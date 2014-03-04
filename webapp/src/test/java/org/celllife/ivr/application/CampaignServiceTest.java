package org.celllife.ivr.application;

import junit.framework.Assert;
import org.celllife.ivr.domain.Campaign;
import org.celllife.ivr.domain.CampaignType;
import org.celllife.ivr.test.TestConfiguration;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public class CampaignServiceTest extends TestConfiguration {

    @Autowired
    CampaignService campaignService;

    @Test
    public void testAddMessagesToCampaign() throws Exception {

        Campaign campaign = new Campaign("test", CampaignType.DAILY, "test campaign", 1, 3, "","","");
        campaign = campaignService.saveCampaign(campaign);

        List<Integer> verboiceMessageNumbers = new ArrayList<>();

        verboiceMessageNumbers.add(1);
        verboiceMessageNumbers.add(2);
        verboiceMessageNumbers.add(3);

        List<Date> timesOfMessages = new ArrayList<>();

        Date timeOfMessage = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 9);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        timeOfMessage = cal.getTime();

        timesOfMessages.add(timeOfMessage);
        timesOfMessages.add(timeOfMessage);
        timesOfMessages.add(timeOfMessage);

        campaignService.setMessagesForCampaign(campaign.getId(),verboiceMessageNumbers,timesOfMessages);

        Trigger[] triggers = campaignService.getScheduler().getTriggersOfJob("relativeCampaignJobRunner", "campaignJobs");
        Assert.assertEquals(1,triggers.length);

        Calendar triggerCalendar = Calendar.getInstance();
        triggerCalendar.setTime(triggers[0].getNextFireTime());
        Assert.assertEquals(9,triggerCalendar.get(Calendar.HOUR));
        Assert.assertEquals(0,triggerCalendar.get(Calendar.MINUTE));

    }

    @After
    public void tearDown() throws Exception {

        List<Campaign> allCampaigns = campaignService.findAllCampaigns();
        for (Campaign campaign : allCampaigns) {
            campaignService.getScheduler().deleteJob("relativeCampaignJobRunner", "campaignJobs");
        }

        campaignService.deleteAllCampaigns();

    }

}
