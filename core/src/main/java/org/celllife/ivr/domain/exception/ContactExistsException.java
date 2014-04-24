package org.celllife.ivr.domain.exception;

public class ContactExistsException extends IvrException {

    private static final long serialVersionUID = -1447846021318096778L;

    public ContactExistsException(String message) {
        super(message);
    }

}
