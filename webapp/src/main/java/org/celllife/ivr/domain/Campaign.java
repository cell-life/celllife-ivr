package org.celllife.ivr.domain;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Cacheable
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

	private String name;

	private String description = "";

	private int timesPerDay = 0;

	private int duration = 0;

	private Date endDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

	/*private boolean sendNow;

	private int contactCount = 0;

	private int messageCount = 0;*/

    @Enumerated(EnumType.STRING)
    private CampaignType type;

    @Enumerated(EnumType.STRING)
    private CampaignStatus status;

	/*@OneToMany(fetch = FetchType.LAZY, mappedBy = "campaign")
	@ForeignKey(name="fk_campaign_contactmsgtime", inverseName="fk_contactmsgtime_campaign")
	private List<ContactMsgTime> contactMsgTimes = new ArrayList<ContactMsgTime>();  */

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "campaignId")
	private List<Contact> campaignContacts;

    private String callFlowName;

    private String channelName;

    private String scheduleName;

	/*@Transient
	private boolean rebuildMessages;

	@Transient
	private List<Date> messageTimes;*/
	
	public Campaign() {

	}

	public Campaign(CampaignType type, CampaignStatus status, Date startDate, String name, String description) {
		super();
		this.type = type;
		this.status = status;
		this.startDate = startDate == null ? null : new Date(startDate.getTime());
		this.name = name;
		this.description = description;
	}

	public Campaign(String name, String description, CampaignType type, CampaignStatus status,
                    int campaignDuration, int timesPerDay) {
		this.type = type;
		this.status = status;
		this.startDate = new Date();
		this.duration = campaignDuration;
		this.name = name;
		this.description = description;
		this.timesPerDay = timesPerDay;

	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CampaignType getType() {
		return type;
	}

	public void setType(CampaignType type) {
		this.type = type;
	}

	public CampaignStatus getStatus() {
		return status;
	}

	public void setStatus(CampaignStatus status) {
		this.status = status;
	}

	public Date getStartDate() {
		return startDate == null ? null : new Date(startDate.getTime());
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate == null ? null : new Date(startDate.getTime());;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getTimesPerDay() {
		return timesPerDay;
	}

	public void setTimesPerDay(int timesPerDay) {
		this.timesPerDay = timesPerDay;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public List<Contact> getCampaignContacts() {
		return campaignContacts;
	}

	public void setCampaignContacts(List<Contact> campaignContacts) {
		this.campaignContacts = campaignContacts;
	}

	public Date getEndDate() {
		return endDate == null ? null : new Date(endDate.getTime());
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate == null ? null : new Date(endDate.getTime());
	}

	public boolean isActive() {
		return CampaignStatus.ACTIVE.equals(status);
	}
	
	public boolean isRunning() {
		return CampaignStatus.RUNNING.equals(status);
	}

	public boolean isScheduled() {
		return CampaignStatus.SCHEDULED.equals(status);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Campaign: ").append(name);
		return builder.toString();
	}

    public String getCallFlowName() {
        return callFlowName;
    }

    public void setCallFlowName(String callFlowName) {
        this.callFlowName = callFlowName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getIdentifierString(){
        return this.getClass().getName() + ":" + id;
    }

}
