package org.celllife.ivr.domain.message;

import java.io.Serializable;

public class CampaignMessageDto implements Serializable {

    private static final long serialVersionUID = -7871174595471576371L;

    Integer verboiceMessageNumber;

    String messageTimeOfDay;

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

}
