package org.celllife.ivr.domain.message;

import java.io.Serializable;

public class CampaignMessageDto implements Serializable {

    private static final long serialVersionUID = -7871174595471576371L;

    Integer verboiceMessageNumber;

    String messageTimeOfDay;

    Integer messageDay;

    public Integer getVerboiceMessageNumber() {
        return verboiceMessageNumber;
    }

    public void setVerboiceMessageNumber(Integer verboiceMessageNumber) {
        this.verboiceMessageNumber = verboiceMessageNumber;
    }

    public String getMessageTimeOfDay() {
        return messageTimeOfDay;
    }

    public void setMessageTimeOfDay(String messageTimeOfDay) {
        this.messageTimeOfDay = messageTimeOfDay;
    }

    public Integer getMessageDay() {
        return messageDay;
    }

    public void setMessageDay(Integer messageDay) {
        this.messageDay = messageDay;
    }

}
