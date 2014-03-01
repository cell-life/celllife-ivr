package org.celllife.ivr.domain;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ContactRepository extends PagingAndSortingRepository<Contact, String>  {

    @Query("select new org.celllife.ivr.domain.Contact(c.msisdn, c.password)  " +
            "from Contact c " +
            "where c.msisdn like ('%' || :msisdn || '%')")
    Iterable<Contact> findMsisdnVisitsByMsisdn(@Param("msisdn") String msisdn);

    @Query("select new org.celllife.ivr.domain.Contact(c.msisdn, c.password) " +
            "from Contact c " +
            "where c.campaignId = :campaignId" )
    Iterable<Contact> findContactsInCampaign(@Param("campaignId") Long campaignId);

}
