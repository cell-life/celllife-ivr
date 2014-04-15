package org.celllife.ivr.domain.verboice.persistedvariables;

import javax.persistence.*;
import java.util.Date;

@Entity
public class PersistedVariables {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    String value;

    Date createdAt;

    Date updatedAt;

    Integer contactId;

    Integer projectVariableId;

    public PersistedVariables() {
    }

    public PersistedVariables(String value, Date createdAt, Date updatedAt, Integer contactId, Integer projectVariableId) {
        this.value = value;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.contactId = contactId;
        this.projectVariableId = projectVariableId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public Integer getProjectVariableId() {
        return projectVariableId;
    }

    public void setProjectVariableId(Integer projectVariable) {
        this.projectVariableId = projectVariable;
    }

    @Override
    public String toString() {
        // TODO: Implement toString()
        return "";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PersistedVariables other = (PersistedVariables) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
