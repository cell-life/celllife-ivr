package org.celllife.ivr.application.calllog;

import org.apache.commons.collections.IteratorUtils;
import org.celllife.ivr.domain.callog.CallLog;
import org.celllife.ivr.domain.callog.CallLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional("transactionManager")
public class CallLogServiceImpl implements CallLogService{

    private static Logger log = LoggerFactory.getLogger(CallLogServiceImpl.class);

    @Autowired
    CallLogRepository callLogRepository;

    @Override
    public CallLog getCallLog(Long id) {
        return callLogRepository.findOne(id);
    }

    @Override
    public CallLog saveCallLog(CallLog callLog) {
        return callLogRepository.save(callLog);
    }

    @Override
    public CallLog findCallLogByVerboiceId(Long verboiceId) {
        if (((IteratorUtils.toList(callLogRepository.findByVerboiceId(verboiceId).iterator())).size()) != 1) {
           return null;
        }
        return callLogRepository.findByVerboiceId(verboiceId).iterator().next();
    }


}
