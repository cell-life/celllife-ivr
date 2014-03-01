package org.celllife.ivr.application;

public interface VerboiceApplicationService {

    public void enqueueCallForMsisdn(String channelName, String callFlowName, String scheduleName, String msisdn, String password, int messageNumber) throws Exception;

}
