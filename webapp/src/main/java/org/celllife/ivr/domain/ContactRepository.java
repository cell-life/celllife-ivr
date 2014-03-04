package org.celllife.ivr.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ContactRepository extends PagingAndSortingRepository<Contact, String> {

    @Query("select new org.celllife.ivr.domain.Contact(c.msisdn, c.password)  " +
            "from Contact c " +
            "where c.msisdn like ('%' || :msisdn || '%')")
    Iterable<Contact> findMsisdnVisitsByMsisdn(@Param("msisdn") String msisdn);

    @Query("select new org.celllife.ivr.domain.Contact(c.msisdn, c.password) " +
            "from Contact c " +
            "where c.campaignId = :campaignId")
    Iterable<Contact> findContactsInCampaign(@Param("campaignId") Long campaignId);

}
