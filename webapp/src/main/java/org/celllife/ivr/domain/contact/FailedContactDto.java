package org.celllife.ivr.domain.contact;

import java.io.Serializable;

public class FailedContactDto implements Serializable {

    private static final long serialVersionUID = 4778561416998723875L;

    public FailedContactDto(String msisdn) {
        this.msisdn = msisdn;
    }

    private String msisdn;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }
}

