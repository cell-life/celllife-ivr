package org.celllife.ivr.application.calllog;

import org.celllife.ivr.domain.calllog.CallLog;

import java.util.Date;
import java.util.List;

public interface CallLogService {

    /**
     * Gets the call log by id.
     * @param id
     * @return
     */
    CallLog getCallLog(Long id);

    /**
     * Saves a call lof.
     * @param callLog
     * @return
     */
    CallLog saveCallLog(CallLog callLog);

    /**
     * Finds the call log by the remote verboice id.
     * @param verboiceId
     * @return
     */
    CallLog findCallLogByVerboiceId(Long verboiceId);

    /**
     * Finds the call log by the verboice id, msisdn and message number.
     * @param verboiceProjectId
     * @param msisdn
     * @param messageNumber
     * @return
     */
    CallLog findCallLogByVerboiceIdAndMsisdnAndMessageNumber(Integer verboiceProjectId, String msisdn, Integer messageNumber);

    /**
     * Finds all call logs from the current day, by state and retry status.
     * @param state
     * @param retryDone
     * @param date
     * @return
     */
    List<CallLog> findByStateAndRetryDoneAndDateGreaterThan(String state, Boolean retryDone, Date date);

    /**
     * Deletes all call logs. Should only be used in unit tests.
     */
    void deleteAll();

}
