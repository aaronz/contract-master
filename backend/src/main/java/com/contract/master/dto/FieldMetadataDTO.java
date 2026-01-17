package com.contract.master.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FieldMetadataDTO {
    private Long id;
    private String fieldCode;
    private String fieldName;
    private String fieldType;
    private String source;
    private Boolean isVisible;
    private Boolean apiReturn;
    private Integer displayOrder;
    private String fieldColor;
    private String fieldStyles;

    public FieldMetadataDTO(String fieldCode, String fieldName, String fieldType, String source) {
        this.fieldCode = fieldCode;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.source = source;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFieldCode() { return fieldCode; }
    public void setFieldCode(String fieldCode) { this.fieldCode = fieldCode; }
    public String getFieldName() { return fieldName; }
    public void setFieldName(String fieldName) { this.fieldName = fieldName; }
    public String getFieldType() { return fieldType; }
    public void setFieldType(String fieldType) { this.fieldType = fieldType; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public Boolean getIsVisible() { return isVisible; }
    public void setIsVisible(Boolean isVisible) { this.isVisible = isVisible; }
    public Boolean getApiReturn() { return apiReturn; }
    public void setApiReturn(Boolean apiReturn) { this.apiReturn = apiReturn; }
    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    public String getFieldColor() { return fieldColor; }
    public void setFieldColor(String fieldColor) { this.fieldColor = fieldColor; }
    public String getFieldStyles() { return fieldStyles; }
    public void setFieldStyles(String fieldStyles) { this.fieldStyles = fieldStyles; }
}
