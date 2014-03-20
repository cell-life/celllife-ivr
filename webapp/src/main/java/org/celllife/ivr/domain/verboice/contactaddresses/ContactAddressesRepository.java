package org.celllife.ivr.domain.verboice.contactaddresses;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ContactAddressesRepository extends PagingAndSortingRepository<ContactAddresses,Integer> {

    @Query("select new org.celllife.ivr.domain.verboice.contactaddresses.ContactAddresses(c.address, c.contactId, c.projectId, c.createdAt, c.updatedAt) from ContactAddresses c " +
            "where address LIKE :address")
    Iterable<ContactAddresses> findByAddress(@Param("address")String address);

    @Query("select new org.celllife.ivr.domain.verboice.contactaddresses.ContactAddresses(c.address, c.contactId, c.projectId, c.createdAt, c.updatedAt) from ContactAddresses c " +
            "where address LIKE :address and projectId= :projectId")
    Iterable<ContactAddresses> findByAddressAndProject(@Param("address")String address, @Param("projectId") Integer projectId);

    @Query("select count(*) from ContactAddresses c where address like :address")
    Long findTotalContactsWithMsisdn(@Param("address")String msisdn);

    @Query("select count(*) from ContactAddresses c where address like :address and projectId= :projectId")
    Long findTotalContactsWithMsisdnInProject(@Param("address")String msisdn, @Param("projectId") Integer projectId);

}
