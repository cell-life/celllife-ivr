package org.celllife.ivr.domain;

import java.io.Serializable;

public class CampaignDto implements Serializable {

    String name;

    String description;

    Integer timesPerDay;

    Integer duration;

    String callFlowName;

    String channelName;

    String scheduleName;

    public CampaignDto(Campaign campaign) {
        this.name = campaign.getName();
        this.description = campaign.getDescription();
        this.timesPerDay = campaign.getTimesPerDay();
        this.duration = campaign.getDuration();
        this.callFlowName = campaign.getCallFlowName();
        this.channelName = campaign.getChannelName();
        this.scheduleName = campaign.getScheduleName();
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
}
