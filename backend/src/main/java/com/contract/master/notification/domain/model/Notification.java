package com.contract.master.notification.domain.model;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.contract.master.shared.domain.model.BaseTenantEntity;

import java.util.Objects;

@Entity
@Table(name = "notification")
@EntityListeners(AuditingEntityListener.class)
public class Notification extends BaseTenantEntity {
    // ID is now inherited from BaseEntity
    // No need for explicit ID field here

    @Column(name = "user_id", length = 64)
    private String userId;

    @Column(name = "title", length = 128)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "type", length = 32)
    private String type;

    @Column(name = "status", length = 32)
    private String status;

    // Getters and setters for inherited ID should use super.getId()/setId()
    // For other fields, keep existing getters/setters

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean read) {
        isRead = read;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // Compare parent class fields, including inherited ID
        Notification that = (Notification) o;
        // Compare fields specific to Notification
        return Objects.equals(userId, that.userId) &&
               Objects.equals(title, that.title) &&
               Objects.equals(content, that.content) &&
               Objects.equals(isRead, that.isRead) &&
               Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, title, content, isRead, type);
    }
}
