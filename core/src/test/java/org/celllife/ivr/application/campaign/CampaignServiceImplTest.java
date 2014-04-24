package org.celllife.ivr.application.campaign;

import junit.framework.Assert;
import org.celllife.ivr.domain.message.CampaignMessageDto;
import org.celllife.ivr.test.TestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class CampaignServiceImplTest extends TestConfiguration {

    CampaignServiceImpl campaignServiceImpl = new CampaignServiceImpl();

    @Test
    public void testFindCampaignMessagesForDay() {

        List<CampaignMessageDto> campaignMessageDtos = new ArrayList<>();

        CampaignMessageDto campaignMessageDto = new CampaignMessageDto();
        campaignMessageDto.setMessageDay(1);
        campaignMessageDtos.add(campaignMessageDto);

        campaignMessageDto = new CampaignMessageDto();
        campaignMessageDto.setMessageDay(2);
        campaignMessageDto.setMessageTimeOfDay("19:00");
        campaignMessageDtos.add(campaignMessageDto);

        campaignMessageDto = new CampaignMessageDto();
        campaignMessageDto.setMessageDay(2);
        campaignMessageDto.setMessageTimeOfDay("02:30");
        campaignMessageDtos.add(campaignMessageDto);

        campaignMessageDto = new CampaignMessageDto();
        campaignMessageDto.setMessageDay(2);
        campaignMessageDto.setMessageTimeOfDay("02:00");
        campaignMessageDtos.add(campaignMessageDto);

        campaignMessageDto = new CampaignMessageDto();
        campaignMessageDto.setMessageDay(2);
        campaignMessageDto.setMessageTimeOfDay("15:00");
        campaignMessageDtos.add(campaignMessageDto);

        campaignMessageDto = new CampaignMessageDto();
        campaignMessageDto.setMessageDay(2);
        campaignMessageDto.setMessageTimeOfDay("09:00");
        campaignMessageDtos.add(campaignMessageDto);

        campaignMessageDto = new CampaignMessageDto();
        campaignMessageDto.setMessageDay(3);
        campaignMessageDtos.add(campaignMessageDto);

        List<CampaignMessageDto> returnDtos = campaignServiceImpl.findAndSortCampaignMessagesForDay(2, campaignMessageDtos);

        Assert.assertEquals(5,returnDtos.size());
        Assert.assertEquals("02:00",returnDtos.get(0).getMessageTimeOfDay());
        Assert.assertEquals("19:00",returnDtos.get(4).getMessageTimeOfDay());

    }

}
