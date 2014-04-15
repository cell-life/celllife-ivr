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
@Transactional("transactionManager")
public class CampaignMessageServiceImpl implements CampaignMessageService {

    @Autowired
    CampaignMessageRepository campaignMessageRepository;

    @Override
    public void save(CampaignMessage campaignMessage) {
        campaignMessageRepository.save(campaignMessage);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CampaignMessage> findMessagesForTimeSlot(Long campaignId, Date messageTime, Integer messageSlot) {
        return IteratorUtils.toList(campaignMessageRepository.findMessagesForTimeSlot(campaignId,messageTime,messageSlot).iterator());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CampaignMessage> findMessagesInCampaign(Long campaignId) {
        return IteratorUtils.toList(campaignMessageRepository.findMessagesForCampaign(campaignId).iterator());
    }

    @Override
    public void deleteMessage(Long campaignMessageId) {
        campaignMessageRepository.delete(campaignMessageId);
    }

}
