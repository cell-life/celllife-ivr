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
    @Enumerated(EnumType.STRING)
    private CampaignType type;

    @Enumerated(EnumType.STRING)
    private CampaignStatus status;
    private String callFlowName;

    private String channelName;

    private String scheduleName;

	public Campaign() {

	}

    public Campaign(String name, CampaignType type, String description, int timesPerDay, int duration, String callFlowName, String channelName, String scheduleName) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.timesPerDay = timesPerDay;
        this.duration = duration;
        this.callFlowName = callFlowName;
        this.channelName = channelName;
        this.scheduleName = scheduleName;
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
