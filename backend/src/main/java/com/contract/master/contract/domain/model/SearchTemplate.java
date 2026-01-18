package com.contract.master.contract.domain.model;

import jakarta.persistence.*;
import com.contract.master.shared.domain.model.BaseTenantEntity;

import java.util.Objects;

@Entity
@Table(name = "search_template")
public class SearchTemplate extends BaseTenantEntity {
    // ID is now inherited from BaseEntity
    // No need for explicit ID field here

    @Column(name = "template_name", length = 128)
    private String templateName;

    @Column(name = "user_id", length = 64)
    private String userId;

    @Column(name = "filter_json", columnDefinition = "TEXT")
    private String filterJson;

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFilterJson() {
        return filterJson;
    }

    public void setFilterJson(String filterJson) {
        this.filterJson = filterJson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // Compare parent class fields, including inherited ID
        SearchTemplate that = (SearchTemplate) o;
        return Objects.equals(templateName, that.templateName) &&
               Objects.equals(userId, that.userId) &&
               Objects.equals(filterJson, that.filterJson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), templateName, userId, filterJson);
    }
}
