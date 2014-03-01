package org.celllife.ivr.application;

import org.celllife.ivr.domain.CampaignMessage;
import org.celllife.ivr.domain.CampaignMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CampaignMessageServiceImpl implements CampaignMessageService {

    @Autowired
    CampaignMessageRepository campaignMessageRepository;

    @Override
    public void save(CampaignMessage campaignMessage) {
        campaignMessageRepository.save(campaignMessage);
    }
}
