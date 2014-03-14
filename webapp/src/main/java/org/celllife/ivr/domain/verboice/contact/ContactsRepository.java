package org.celllife.ivr.domain.verboice.contact;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ContactsRepository extends PagingAndSortingRepository<Contacts, Integer> {


}
