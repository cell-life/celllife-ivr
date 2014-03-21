package org.celllife.ivr.application.message;

import org.celllife.ivr.domain.message.CampaignMessage;
import org.celllife.ivr.domain.message.CampaignMessageRepository;
import org.dozer.util.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CampaignMessageServiceImpl implements CampaignMessageService {

    @Autowired
    CampaignMessageRepository campaignMessageRepository;

    @Override
    @Transactional("transactionManager")
    public void save(CampaignMessage campaignMessage) {
        campaignMessageRepository.save(campaignMessage);
    }

    @Override
    @Transactional("transactionManager")
    public List<CampaignMessage> findMessagesForTimeSlot(Long campaignId, Date messageTime, Integer messageSlot) {
        return IteratorUtils.toList(campaignMessageRepository.findMessagesForTimeSlot(campaignId,messageTime,messageSlot).iterator());
    }

    @Override
    @Transactional("transactionManager")
    public List<CampaignMessage> findMessagesInCampaign(Long campaignId) {
        return IteratorUtils.toList(campaignMessageRepository.findMessagesForCampaign(campaignId).iterator());
    }

    @Override
    @Transactional("transactionManager")
    public void deleteMessage(Long campaignMessageId) {
        campaignMessageRepository.delete(campaignMessageId);
    }

}
