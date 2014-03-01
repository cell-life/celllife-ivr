package org.celllife.ivr.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Cacheable
public class CampaignMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
	
	private int verboiceMessageNumber;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition="DATETIME")
	private Date messageTime;
	
	private int messageSlot;

	private int messageDay;

    private Long campaignId;

	public CampaignMessage() {
	}

	public CampaignMessage(int verboiceMessageNumber, int msgDay, int msgSlot, Date messageTime) {
		this.verboiceMessageNumber = verboiceMessageNumber;
		this.messageDay = msgDay;
		this.messageTime = messageTime;
		this.messageSlot = msgSlot;
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
}
