package org.celllife.ivr.application;

import org.apache.commons.collections.ListUtils;
import org.celllife.ivr.domain.CampaignMessage;
import org.celllife.ivr.domain.CampaignMessageRepository;
import org.dozer.util.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CampaignMessageServiceImpl implements CampaignMessageService {

    @Autowired
    CampaignMessageRepository campaignMessageRepository;

    @Override
    public void save(CampaignMessage campaignMessage) {
        campaignMessageRepository.save(campaignMessage);
    }

    @Override
    public List<CampaignMessage> findMessagesForTimeSlot(Long campaignId, Date messageTime, Integer messageSlot) {
        return IteratorUtils.toList(campaignMessageRepository.findMessagesForTimeSlot(campaignId,messageTime,messageSlot).iterator());
    }
}
