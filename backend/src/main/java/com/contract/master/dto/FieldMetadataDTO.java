package com.contract.master.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class FieldMetadataDTO {
    private String fieldCode;
    private String fieldName;
    private String fieldType;
    private String source;

    public FieldMetadataDTO() {}

    public FieldMetadataDTO(String fieldCode, String fieldName, String fieldType, String source) {
        this.fieldCode = fieldCode;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.source = source;
    }

    public String getFieldCode() { return fieldCode; }
    public void setFieldCode(String fieldCode) { this.fieldCode = fieldCode; }
    public String getFieldName() { return fieldName; }
    public void setFieldName(String fieldName) { this.fieldName = fieldName; }
    public String getFieldType() { return fieldType; }
    public void setFieldType(String fieldType) { this.fieldType = fieldType; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
}
