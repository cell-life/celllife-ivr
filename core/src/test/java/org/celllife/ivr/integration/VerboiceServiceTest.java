package org.celllife.ivr.integration;

import org.celllife.ivr.integration.verboice.VerboiceService;
import org.celllife.ivr.test.TestConfiguration;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class VerboiceServiceTest extends TestConfiguration {

    @Autowired
    VerboiceService verboiceService;

    @Ignore
    @Test
    public void testEnqueueCall() throws Exception {

        String response = verboiceService.enqueueCallWithPassword("Skype Channel", "Stellenbosch Test", "Dine Schedule", "27724194158", 100, "2222");

    }


}
