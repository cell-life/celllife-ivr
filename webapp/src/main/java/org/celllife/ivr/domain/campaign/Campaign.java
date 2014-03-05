package org.celllife.ivr.domain.campaign;

import javax.persistence.*;
import java.util.Date;

@Entity
@Cacheable
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Basic(optional=false)
    private String name;

	private String description = "";

    @Basic(optional=false)
    private int timesPerDay = 0;

    @Basic(optional=false)
    private int duration = 0;

	private Date endDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Enumerated(EnumType.STRING)
    private CampaignType type;

    @Enumerated(EnumType.STRING)
    private CampaignStatus status;

    @Basic(optional=false)
    private String callFlowName;

    @Basic(optional=false)
    private String channelName;

    @Basic(optional=false)
    private String scheduleName;

	public Campaign() {

	}

    public Campaign(Long id, String name, CampaignType type, String description, int timesPerDay, int duration, String callFlowName, String channelName, String scheduleName) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.timesPerDay = timesPerDay;
        this.duration = duration;
        this.callFlowName = callFlowName;
        this.channelName = channelName;
        this.scheduleName = scheduleName;
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

    public CampaignDto getCampaignDto() {
        CampaignDto campaignDto = new CampaignDto();
        campaignDto.setName(this.getName());
        campaignDto.setDescription(this.getDescription());
        campaignDto.setDuration(this.getDuration());
        campaignDto.setTimesPerDay(this.getTimesPerDay());
        campaignDto.setCallFlowName(this.getCallFlowName());
        campaignDto.setChannelName(this.getChannelName());
        campaignDto.setScheduleName(this.getScheduleName());
        return campaignDto;
    }

}
