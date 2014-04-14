package org.celllife.ivr.domain.campaign;

import org.celllife.ivr.domain.contact.Contact;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Cacheable
public class Campaign implements Serializable {

    private static final long serialVersionUID = 3718726486523331541L;

    @Id
    @TableGenerator(
            name="CampaignIdGen",
            table="hibernate_sequences",
            pkColumnName="sequence_name",
            valueColumnName="sequence_next_hi_value",
            pkColumnValue="alert",
            initialValue=1,
            allocationSize=1)
    @GeneratedValue(strategy=GenerationType.TABLE, generator="CampaignIdGen")
    private Long id;

    @Basic(optional=false)
    private String name;

	private String description = "";

    @Basic(optional=false)
    private int duration = 0;

	private Date endDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Enumerated(EnumType.STRING)
    private CampaignStatus status;

    @Basic(optional=false)
    private String callFlowName;

    @Basic(optional=false)
    private String channelName;

    @Basic(optional=false)
    private String scheduleName;

    @Basic(optional=false)
    private Long verboiceProjectId;

	public Campaign() {

	}

    public Campaign(Long id, String name, CampaignType type, String description, int timesPerDay, int duration, String callFlowName, String channelName, String scheduleName, Long verboiceProjectId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.callFlowName = callFlowName;
        this.channelName = channelName;
        this.scheduleName = scheduleName;
        this.verboiceProjectId = verboiceProjectId;
    }

    public Campaign(String name, CampaignType type, String description, int timesPerDay, int duration, String callFlowName, String channelName, String scheduleName, Long verboiceProjectId) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.callFlowName = callFlowName;
        this.channelName = channelName;
        this.scheduleName = scheduleName;
        this.verboiceProjectId = verboiceProjectId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getVerboiceProjectId() {
        return verboiceProjectId;
    }

    public void setVerboiceProjectId(Long verboiceProjectId) {
        this.verboiceProjectId = verboiceProjectId;
    }

    public CampaignDto getCampaignDto() {
        CampaignDto campaignDto = new CampaignDto();
        campaignDto.setId(this.getId());
        campaignDto.setName(this.getName());
        campaignDto.setDescription(this.getDescription());
        campaignDto.setDuration(this.getDuration());
        campaignDto.setCallFlowName(this.getCallFlowName());
        campaignDto.setChannelName(this.getChannelName());
        campaignDto.setScheduleName(this.getScheduleName());
        campaignDto.setVerboiceProjectId(this.getVerboiceProjectId());
        return campaignDto;
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
        Campaign other = (Campaign) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
