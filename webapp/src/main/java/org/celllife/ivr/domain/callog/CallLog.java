package org.celllife.ivr.domain.callog;

import javax.persistence.*;
import java.util.Date;

@Entity
@Cacheable
public class CallLog {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Basic(optional=false)
    private Date date;

    private Long verboiceId;

    @Basic(optional=false)
    private String msisdn;

    private String password;

    private int messageNumber;

    private String channelName;

    private String callFlowName;

    private String scheduleName;

    private String state;

    public CallLog() {

    }

    public CallLog(Long id, Date date, Long verboiceId, String msisdn, String password, int progress, String channelName, String callFlowName, String scheduleName, String state, int messageNumber) {
        this.id = id;
        this.date = date;
        this.verboiceId = verboiceId;
        this.msisdn = msisdn;
        this.password = password;
        this.messageNumber = messageNumber;
        this.channelName = channelName;
        this.callFlowName = callFlowName;
        this.scheduleName = scheduleName;
        this.state = state;
    }

    public CallLog(Date date, Long verboiceId, String msisdn, String password, int progress, String channelName, String callFlowName, String scheduleName, String state, int messageNumber) {
        this.date = date;
        this.verboiceId = verboiceId;
        this.msisdn = msisdn;
        this.password = password;
        this.messageNumber = messageNumber;
        this.channelName = channelName;
        this.callFlowName = callFlowName;
        this.scheduleName = scheduleName;
        this.state = state;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getVerboiceId() {
        return verboiceId;
    }

    public void setVerboiceId(Long verboiceId) {
        this.verboiceId = verboiceId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(int progress) {
        this.messageNumber = progress;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getCallFlowName() {
        return callFlowName;
    }

    public void setCallFlowName(String callFlowName) {
        this.callFlowName = callFlowName;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
