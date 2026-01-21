package com.contract.master.contract.application;

import com.contract.master.contract.domain.model.ContractExtendField;
import com.contract.master.contract.domain.repository.ContractExtendFieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ExtendFieldService {

    @Autowired
    private ContractExtendFieldRepository repository;

    public List<ContractExtendField> getAllFields() {
        return repository.findAll();
    }

    @Transactional
    @CacheEvict(value = "metadata", allEntries = true)
    public ContractExtendField createField(ContractExtendField field) {
        if (field.getFieldId() == null) {
            field.setFieldId(UUID.randomUUID().toString());
        }
        return repository.save(field);
    }

    @Transactional
    @CacheEvict(value = "metadata", allEntries = true)
    public void deleteField(Long id) {
        repository.deleteById(id);
    }
}
