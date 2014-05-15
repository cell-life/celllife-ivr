package org.celllife.ivr.domain.callog;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * This entity is used to log/record a call sent to the Verboice server.
 */

@Entity
public class CallLog implements Serializable {

    private static final long serialVersionUID = -6311433560024750172L;

    @Id
    @TableGenerator(
            name="CallLogIdGen",
            table="hibernate_sequences",
            pkColumnName="sequence_name",
            valueColumnName="sequence_next_hi_value",
            pkColumnValue="alert",
            initialValue=1,
            allocationSize=1)
    @GeneratedValue(strategy=GenerationType.TABLE, generator="CallLogIdGen")
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

    private String passwordEntered;

    private Integer verboiceProjectId;

    @Column(columnDefinition = "BIT", length = 1)
    private Boolean retryDone;

    private Integer attempt;

    public CallLog() {

    }

    public CallLog(Long id, Date date, Long verboiceId, String msisdn, String channelName, String callFlowName, String scheduleName, String state, int messageNumber, Integer verboiceProjectId, Integer attempt, Boolean retryDone) {
        this.id = id;
        this.date = date;
        this.verboiceId = verboiceId;
        this.msisdn = msisdn;
        this.messageNumber = messageNumber;
        this.channelName = channelName;
        this.callFlowName = callFlowName;
        this.scheduleName = scheduleName;
        this.state = state;
        this.verboiceProjectId = verboiceProjectId;
        this.attempt = attempt;
        this.retryDone = retryDone;
    }

    public CallLog(Date date, Long verboiceId, String msisdn, String channelName, String callFlowName, String scheduleName, String state, int messageNumber, String password, Integer verboiceProjectId , Integer attempt, Boolean retryDone) {
        this.date = date;
        this.verboiceId = verboiceId;
        this.msisdn = msisdn;
        this.messageNumber = messageNumber;
        this.channelName = channelName;
        this.callFlowName = callFlowName;
        this.scheduleName = scheduleName;
        this.state = state;
        this.password = password;
        this.verboiceProjectId = verboiceProjectId;
        this.attempt = attempt;
        this.retryDone = retryDone;
    }

    public Long getId() {
        return id;
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

    public String getPasswordEntered() {
        return passwordEntered;
    }

    public void setPasswordEntered(String passwordEntered) {
        this.passwordEntered = passwordEntered;
    }

    public Integer getVerboiceProjectId() {
        return verboiceProjectId;
    }

    public void setVerboiceProjectId(Integer verboiceProjectId) {
        this.verboiceProjectId = verboiceProjectId;
    }

    public Boolean getRetryDone() {
        return retryDone;
    }

    public void setRetryDone(Boolean retry) {
        this.retryDone = retry;
    }

    public Integer getAttempt() {
        return attempt;
    }

    public void setAttempt(Integer attempt) {
        this.attempt = attempt;
    }

    @Override
    public String toString() {
        return "CallLog [id=" + id + ", date=" + date + ", verboiceId=" + verboiceId + ", msisdn=" + msisdn + ", password="
                + password + ", messageNumber=" + messageNumber + ", channelName=" + channelName + ", callFlowName" + callFlowName
                + ", scheduleName" + scheduleName + ", state" + state +  "]";
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
        CallLog other = (CallLog) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
