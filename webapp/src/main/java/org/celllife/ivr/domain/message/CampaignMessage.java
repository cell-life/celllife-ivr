package org.celllife.ivr.domain.message;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Cacheable
public class CampaignMessage implements Serializable {

    private static final long serialVersionUID = -6551411740591583081L;

    @Id
    @TableGenerator(
            name="CampaignMessageIdGen",
            table="hibernate_sequences",
            pkColumnName="sequence_name",
            valueColumnName="sequence_next_hi_value",
            pkColumnValue="alert",
            initialValue=1,
            allocationSize=1)
    @GeneratedValue(strategy=GenerationType.TABLE, generator="CampaignMessageIdGen")
    private Long id;

    @Basic(optional=false)
    private int verboiceMessageNumber;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition="TIME")
    @Basic(optional=false)
    private Date messageTime;

    @Basic(optional=false)
    private int messageSlot;

    @Basic(optional=false)
    private int messageDay;

    @Basic(optional=false)
    private Long campaignId;

	public CampaignMessage() {
	}

	public CampaignMessage(int verboiceMessageNumber, int msgDay, int msgSlot, Date messageTime, Long campaignId) {
		this.verboiceMessageNumber = verboiceMessageNumber;
		this.messageDay = msgDay;
		this.messageTime = messageTime;
		this.messageSlot = msgSlot;
        this.campaignId = campaignId;
	}

    public CampaignMessage(Long id, int verboiceMessageNumber, int msgDay, int msgSlot, Date messageTime, Long campaignId) {
        setId(id);
        this.verboiceMessageNumber = verboiceMessageNumber;
        this.messageDay = msgDay;
        this.messageTime = messageTime;
        this.messageSlot = msgSlot;
        this.campaignId = campaignId;
    }

	public int getVerboiceMessageNumber() {
		return verboiceMessageNumber;
	}

	public void setVerboiceMessageNumber(int message) {
		this.verboiceMessageNumber = message;
	}

	public Date getMsgDate() {
		return messageTime == null ? null : new Date();
	}

	public void setMsgDate(Date msgDate) {
		this.messageTime = msgDate == null ? null : new Date();
	}
	
	public int getMessageSlot() {
		return messageSlot;
	}

	public void setMessageSlot(int msgSlot) {
		this.messageSlot = msgSlot;
	}

	public void setMessageDay(int msgDay) {
		this.messageDay = msgDay;
	}

	public int getMessageDay() {
		return messageDay;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Date getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Date msgDateTime) {
        this.messageTime = msgDateTime;
    }

    @Override
	public String toString() {
		return "CampaignMessage [message=" + verboiceMessageNumber + ", msgDate=" + messageTime
				+ ", msgTime=" + messageTime + ", msgSlot=" + messageSlot + ", msgDay="
				+ messageDay + "]";
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
        CampaignMessage other = (CampaignMessage) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
