package org.celllife.ivr.application.calllog;

import junit.framework.Assert;
import org.celllife.ivr.domain.callog.CallLog;
import org.celllife.ivr.test.TestConfiguration;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class CallLogServiceTest extends TestConfiguration {

    @Autowired
    CallLogService callLogService;

    @Test
    public void testFindByStateAndDateGreaterThan() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR,-1);
        Date oneDayBefore = cal.getTime();

        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR,-5);
        Date fiveDaysBefore = cal.getTime();

        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date midnightToday = cal.getTime();

        CallLog callLog = new CallLog();
        callLog.setDate(new Date());
        callLog.setState("completed");
        callLog.setRetryDone(false);
        callLogService.saveCallLog(callLog);

        callLog = new CallLog();
        callLog.setDate(fiveDaysBefore);
        callLog.setState("completed");
        callLog.setRetryDone(false);
        callLogService.saveCallLog(callLog);

        callLog = new CallLog();
        callLog.setDate(new Date());
        callLog.setState("ringing");
        callLog.setRetryDone(false);
        callLogService.saveCallLog(callLog);

        List<CallLog> callLogList = callLogService.findByStateAndRetryDoneAndDateGreaterThan("completed",false,oneDayBefore);
        Assert.assertEquals(1,callLogList.size());

        callLogList = callLogService.findByStateAndRetryDoneAndDateGreaterThan("completed",false,midnightToday);
        Assert.assertEquals(1,callLogList.size());

    }

    @After
    public void tearDown() {

        callLogService.deleteAll();

    }

}
