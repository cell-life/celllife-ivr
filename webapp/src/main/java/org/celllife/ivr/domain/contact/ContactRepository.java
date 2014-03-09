package org.celllife.ivr.domain.contact;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ContactRepository extends PagingAndSortingRepository<Contact, Long> {

    @Query("select new org.celllife.ivr.domain.contact.Contact(c.id, c.msisdn, c.password, c.campaignId, c.progress)  " +
            "from Contact c " +
            "where c.msisdn like ('%' || :msisdn || '%')")
    Iterable<Contact> findContactByMsisdn(@Param("msisdn") String msisdn);

    @Query("select new org.celllife.ivr.domain.contact.Contact(c.id, c.msisdn, c.password, c.campaignId, c.progress) " +
            "from Contact c " +
            "where c.campaignId = :campaignId")
    Iterable<Contact> findContactsInCampaign(@Param("campaignId") Long campaignId);

}
