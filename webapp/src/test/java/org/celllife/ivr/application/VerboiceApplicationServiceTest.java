package org.celllife.ivr.application;

import org.celllife.ivr.application.verboice.VerboiceApplicationService;
import org.celllife.ivr.test.TestConfiguration;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
public class VerboiceApplicationServiceTest extends TestConfiguration {

    @Autowired
    VerboiceApplicationService verboiceApplicationService;

    @Ignore
    @Test
    public void testEnqueueCallForMsisdn() throws Exception{

        verboiceApplicationService.enqueueCallForMsisdn("Skype Channel", "Call #1", "Test", "27721234567", 1, "1234");

    }



}
