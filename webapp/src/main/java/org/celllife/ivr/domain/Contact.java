package org.celllife.ivr.domain;

import javax.persistence.*;

@Entity
@Cacheable
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private String password;

    @Basic(optional=false)
    private String msisdn;

    private int progress = 0;

    private Long campaignId;

    public Contact() {

    }

    public Contact(String msisdn, String password) {
        setMsisdn(msisdn);
        setPassword(password);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
