package org.celllife.ivr.application.calllog;

import org.celllife.ivr.domain.callog.CallLog;

public interface CallLogService {

    CallLog getCallLog(Long id);

    CallLog saveCallLog(CallLog callLog);

   CallLog findCallLogByVerboiceId(Long verboiceId);

}
