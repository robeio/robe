package io.robe.admin.hibernate.entity;

import io.robe.hibernate.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class ActionLog extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String actionType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date actionTime = new Date();

    @Column
    private String description;

    @Column
    @Lob
    private String additionalData;

    @Column
    private boolean positive;

    public ActionLog() {

    }

    public ActionLog(String actionType, boolean positive) {
        this.actionType = actionType;
        this.positive = positive;
    }

    public ActionLog(String actionType, String description, boolean positive) {
        this.actionType = actionType;
        this.description = description;
        this.positive = positive;
    }

    public ActionLog(String actionType, String description, String additionalData, boolean positive) {
        this.actionType = actionType;
        this.description = description;
        this.additionalData = additionalData;
        this.positive = positive;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Date getActionTime() {
        return actionTime;
    }

    public void setActionTime(Date actionTime) {
        this.actionTime = actionTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }

    public boolean isPositive() {
        return positive;
    }

    public void setPositive(boolean positive) {
        this.positive = positive;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ActionLog{");
        sb.append("actionType='").append(actionType).append('\'');
        sb.append(", actionTime=").append(actionTime);
        sb.append(", description='").append(description).append('\'');
        sb.append(", additionalData='").append(additionalData).append('\'');
        sb.append(", positive=").append(positive);
        sb.append('}');
        return sb.toString();
    }
}
