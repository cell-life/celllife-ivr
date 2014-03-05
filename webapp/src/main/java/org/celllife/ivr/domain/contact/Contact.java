package org.celllife.ivr.domain.contact;

import javax.persistence.*;

@Entity
@Cacheable
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private String password;

    @Basic(optional=false)
    private String msisdn;

    private int progress = 0;

    @Basic(optional=false)
    private Long campaignId;

    public Contact() {

    }

    public Contact(Long id, String msisdn, String password, Long campaignId, int progress) {
        this.id = id;
        setMsisdn(msisdn);
        setPassword(password);
        setProgress(progress);
        setCampaignId(campaignId);
    }

    public Contact(String msisdn, String password, Long campaignId, int progress) {
        setMsisdn(msisdn);
        setPassword(password);
        setProgress(progress);
        setCampaignId(campaignId);
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

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }
}
