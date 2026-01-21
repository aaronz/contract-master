package com.contract.master.contract.metadata.application;

import com.contract.master.contract.domain.model.ContractExtendField;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class MetadataValidator {

    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^-?\\d+(\\.\\d+)?$");

    public void validate(ContractExtendField field, String value) {
        if (value == null || value.isEmpty()) {
            if (Boolean.TRUE.equals(field.getIsRequired())) {
                throw new IllegalArgumentException("Field '" + field.getFieldName() + "' is required.");
            }
            return;
        }

        String type = field.getFieldType();
        switch (type) {
            case "NUMBER":
                if (!NUMBER_PATTERN.matcher(value).matches()) {
                    throw new IllegalArgumentException("Field '" + field.getFieldName() + "' must be a number.");
                }
                break;
            case "DATE":
                if (!DATE_PATTERN.matcher(value).matches()) {
                    throw new IllegalArgumentException("Field '" + field.getFieldName() + "' must be a date (YYYY-MM-DD).");
                }
                break;
            case "TEXT":
            case "SELECT":
                break;
            default:
                break;
        }
    }
}
