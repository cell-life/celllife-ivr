package org.celllife.ivr.domain.verboice.contacts;

import java.io.Serializable;

public class ContactsDto implements Serializable {

    private static final long serialVersionUID = 3588235151314294411L;

    private String createdAt;

    private String updatedAt;

    private Integer projectId;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

}
