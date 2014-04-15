package org.celllife.ivr.domain.contact;

import org.celllife.ivr.domain.campaign.CampaignDto;
import org.celllife.ivr.domain.exception.InvalidMsisdnException;

import javax.persistence.*;
import java.io.Serializable;

/* This entity is for storing a contact with a unique msisdn (cell phone number) */

@Entity
public class Contact implements Serializable {

    private static final long serialVersionUID = -6710129478435404575L;

    @Id
    @TableGenerator(
            name="ContactIdGen",
            table="hibernate_sequences",
            pkColumnName="sequence_name",
            valueColumnName="sequence_next_hi_value",
            pkColumnValue="alert",
            initialValue=1,
            allocationSize=1)
    @GeneratedValue(strategy=GenerationType.TABLE, generator="ContactIdGen")
    private Long id;

    private String password;

    @Column(unique = true)
    @Basic(optional = false)
    private String msisdn;

    private int progress = 0;

    @Basic(optional=false)
    private Long campaignId;

    private Integer verboiceContactId;

    @Column(columnDefinition = "BIT", length = 1)
    private boolean voided = false;

    public Contact() {

    }

    public Contact(Long id, String msisdn, String password, Long campaignId, int progress) {
        this.id = id;
        setMsisdn(msisdn);
        setPassword(password);
        setProgress(progress);
        setCampaignId(campaignId);
    }

    public Contact(String msisdn, String password, Long campaignId, int progress) throws InvalidMsisdnException {
        if (isMsisdnNumeric(msisdn) == false) {
            throw new InvalidMsisdnException("The number " + msisdn + " contains characters that are not numeric.");
        }
        setMsisdn(msisdn);
        setPassword(password);
        setProgress(progress);
        setCampaignId(campaignId);
    }

    public ContactDto getContactDto() {
        ContactDto contactDto = new ContactDto();
        contactDto.setId(this.getId());
        contactDto.setMsisdn(this.getMsisdn());
        contactDto.setCampaignId(this.getCampaignId());
        contactDto.setProgress(this.getProgress());
        contactDto.setPassword(this.getPassword());
        contactDto.setVoided(this.isVoided());
        return contactDto;
    }

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

    public Integer getVerboiceContactId() {
        return verboiceContactId;
    }

    public void setVerboiceContactId(Integer verboiceContactId) {
        this.verboiceContactId = verboiceContactId;
    }

    public boolean isVoided() {
        return voided;
    }

    public void setVoided(boolean voided) {
        this.voided = voided;
    }

    protected boolean isMsisdnNumeric(String msisdn) {

        if (msisdn.matches("[0-9]+")) {
            return true;
        } else {
            return false;
        }

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
        Contact other = (Contact) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}