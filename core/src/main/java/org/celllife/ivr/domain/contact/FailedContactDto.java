package org.celllife.ivr.domain.contact;

import java.io.Serializable;

public class FailedContactDto implements Serializable {

    private static final long serialVersionUID = 4778561416998723875L;

    public FailedContactDto(String msisdn, String reasonForFailiure) {
        this.msisdn = msisdn;
        this.reasonForFailiure = reasonForFailiure;
    }

    private String msisdn;

    private String reasonForFailiure;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getReasonForFailiure() {
        return reasonForFailiure;
    }

    public void setReasonForFailiure(String reasonForFailiure) {
        this.reasonForFailiure = reasonForFailiure;
    }
}

