package org.celllife.ivr.domain.exception;

public class CampaignNameExistsException extends IvrException {

    private static final long serialVersionUID = -1447846021318096778L;

    public CampaignNameExistsException(String message) {
        super(message);
    }

}
