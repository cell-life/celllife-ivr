package org.celllife.ivr.domain.campaign;

import java.io.Serializable;

public class CampaignDto implements Serializable {

    private static final long serialVersionUID = 4778561416998723875L;

    private long id;

    private String name;
    private String description;
    private Integer timesPerDay;
    private Integer duration;
    private String callFlowName;
    private String channelName;
    private String scheduleName;
    private Long verboiceProjectId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Integer getTimesPerDay() {
        return timesPerDay;
    }

    public void setTimesPerDay(Integer timesPerDay) {
        this.timesPerDay = timesPerDay;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
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

    public Long getVerboiceProjectId() {
        return verboiceProjectId;
    }

    public void setVerboiceProjectId(Long verboiceProjectId) {
        this.verboiceProjectId = verboiceProjectId;
    }
}
