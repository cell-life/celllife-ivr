package org.celllife.ivr.domain.contact;

import java.io.Serializable;

public class ContactDto implements Serializable {

    private static final long serialVersionUID = 3696740543723280622L;

    private Long id;
    private String password;
    private String msisdn;
    private Integer progress = 0;
    private Long campaignId;
    private Integer verboiceContactId;
    private Boolean voided;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Integer getVerboiceContactId() {
        return verboiceContactId;
    }

    public void setVerboiceContactId(Integer verboiceContactId) {
        this.verboiceContactId = verboiceContactId;
    }

    public Boolean getVoided() {
        return voided;
    }

    public void setVoided(Boolean voided) {
        this.voided = voided;
    }
}
