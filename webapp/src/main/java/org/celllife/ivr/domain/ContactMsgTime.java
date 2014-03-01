package org.celllife.ivr.domain;

import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.util.Date;

/*@Entity
@Cacheable
public class ContactMsgTime {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
	
	@Temporal(value= TemporalType.TIME)
	private Date msgTime;

	private int msgSlot;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "campaignId")
    private Campaign campaign;
	
	public ContactMsgTime() {

	}

	public ContactMsgTime(Date msgTime, int msgSlot, Campaign campaign) {
		super();
		this.msgTime = msgTime == null ? null : new Date(msgTime.getTime());
		this.msgSlot = msgSlot;
		this.campaign = campaign;
	}

	public Date getMsgTime() {
		return msgTime == null ? null : new Date(msgTime.getTime());
	}

	public void setMsgTime(Date msgTime) {
		this.msgTime = msgTime == null ? null : new Date(msgTime.getTime());
	}
	
	public int getMsgSlot() {
		return msgSlot;
	}

	public void setMsgSlot(int msgSlot) {
		this.msgSlot = msgSlot;
	}

	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}
}*/
