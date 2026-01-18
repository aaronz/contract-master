package com.contract.master.contract.metadata.application;

import com.contract.master.contract.domain.model.ContractExtendField;
import com.contract.master.contract.domain.repository.ContractExtendFieldRepository;
import com.contract.master.contract.metadata.domain.model.FieldConfig;
import com.contract.master.contract.metadata.domain.repository.FieldConfigRepository;
import com.contract.master.contract.metadata.dto.FieldMetadataDTO;
import com.contract.master.security.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map; // Add this import
import java.util.stream.Collectors;
import com.contract.master.contract.dto.ContractDTO;

import com.contract.master.shared.domain.model.TenantId;
@Service
public class MetadataService {

    @Autowired
    private ContractExtendFieldRepository extendFieldRepository;

    @Autowired
    private FieldConfigRepository fieldConfigRepository;

    private final Map<String, List<FieldConfig>> fieldConfigCache = new java.util.concurrent.ConcurrentHashMap<>();

    public List<FieldMetadataDTO> getContractFields() {
        String tenantId = TenantContext.getCurrentTenant();
        List<FieldMetadataDTO> fields = new ArrayList<>();
        
        List<FieldConfig> configs = fieldConfigRepository.findByTenantId(TenantId.of(tenantId));

        for (Field field : com.contract.master.contract.dto.ContractDTO.class.getDeclaredFields()) {
            String name = field.getName();
            if (name.equals("extendedFields") || name.equals("attachments") || name.equals("tenantId") || name.equals("contractId")) {
                continue;
            }
            
            String fieldCode = camelToSnakeCase(name);
            FieldConfig config = configs.stream().filter(c -> c.getFieldCode().equals(fieldCode)).findFirst().orElse(null);
            
            String type = getMetadataType(field.getType());
            String label = config != null && config.getFieldAlias() != null ? config.getFieldAlias() : camelToWords(name);
            FieldMetadataDTO dto = new FieldMetadataDTO(fieldCode, label, type, "STANDARD");
            applyConfigToDTO(dto, config);
            fields.add(dto);
        }

        List<ContractExtendField> extendFields = extendFieldRepository.findByTenantId(TenantId.of(tenantId));
        for (ContractExtendField ef : extendFields) {
            FieldConfig config = configs.stream().filter(c -> c.getFieldCode().equals(ef.getFieldCode())).findFirst().orElse(null);
            String label = config != null && config.getFieldAlias() != null ? config.getFieldAlias() : ef.getFieldName();
            FieldMetadataDTO dto = new FieldMetadataDTO(ef.getFieldCode(), label, ef.getFieldType(), "EXTEND");
            applyConfigToDTO(dto, config);
            fields.add(dto);
        }

        return fields;
    }

    private String camelToSnakeCase(String camel) {
        if (camel == null) return null;
        StringBuilder result = new StringBuilder();
        for (char c : camel.toCharArray()) {
            if (Character.isUpperCase(c)) {
                result.append("_");
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    private void applyConfigToDTO(FieldMetadataDTO dto, FieldConfig config) {
        if (config != null) {
            dto.setId(config.getId());
            dto.setIsVisible(config.getIsVisible() != null ? config.getIsVisible() : true);
            dto.setApiReturn(config.getApiReturn() != null ? config.getApiReturn() : true);
            dto.setDisplayOrder(config.getDisplayOrder() != null ? config.getDisplayOrder() : 999);
            dto.setFieldColor(config.getFieldColor());
            dto.setFieldStyles(config.getFieldStyles());
        } else {
            dto.setIsVisible(true);
            dto.setApiReturn(true);
            dto.setDisplayOrder(999);
        }
    }

    private String getMetadataType(Class<?> type) {
        if (type == java.math.BigDecimal.class || type == Integer.class || type == Long.class) return "NUMBER";
        if (type == java.time.LocalDate.class || type == java.time.LocalDateTime.class) return "DATE";
        if (type == Boolean.class) return "BOOLEAN";
        return "TEXT";
    }

    private String camelToWords(String camel) {
        StringBuilder result = new StringBuilder();
        for (char c : camel.toCharArray()) {
            if (Character.isUpperCase(c)) {
                result.append(" ");
                result.append(c);
            } else if (result.length() == 0) {
                result.append(Character.toUpperCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public void clearFieldConfigCache() {
        fieldConfigCache.clear();
    }
}
