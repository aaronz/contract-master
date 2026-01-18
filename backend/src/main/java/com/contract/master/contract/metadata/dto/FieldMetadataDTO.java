package com.contract.master.contract.metadata.dto;

import java.util.Objects;

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

    public FieldMetadataDTO() {
    }

    public FieldMetadataDTO(Long id, String fieldCode, String fieldName, String fieldType, String source, Boolean isVisible, Boolean apiReturn, Integer displayOrder, String fieldColor, String fieldStyles) {
        this.id = id;
        this.fieldCode = fieldCode;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.source = source;
        this.isVisible = isVisible;
        this.apiReturn = apiReturn;
        this.displayOrder = displayOrder;
        this.fieldColor = fieldColor;
        this.fieldStyles = fieldStyles;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldMetadataDTO that = (FieldMetadataDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(fieldCode, that.fieldCode) && Objects.equals(fieldName, that.fieldName) && Objects.equals(fieldType, that.fieldType) && Objects.equals(source, that.source) && Objects.equals(isVisible, that.isVisible) && Objects.equals(apiReturn, that.apiReturn) && Objects.equals(displayOrder, that.displayOrder) && Objects.equals(fieldColor, that.fieldColor) && Objects.equals(fieldStyles, that.fieldStyles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fieldCode, fieldName, fieldType, source, isVisible, apiReturn, displayOrder, fieldColor, fieldStyles);
    }

    @Override
    public String toString() {
        return "FieldMetadataDTO{" +
               "id=" + id +
               ", fieldCode='" + fieldCode + '\'' +
               ", fieldName='" + fieldName + '\'' +
               ", fieldType='" + fieldType + '\'' +
               ", source='" + source + '\'' +
               ", isVisible=" + isVisible +
               ", apiReturn=" + apiReturn +
               ", displayOrder=" + displayOrder +
               ", fieldColor='" + fieldColor + '\'' +
               ", fieldStyles='" + fieldStyles + '\'' +
               '}';
    }
}
