package org.celllife.ivr.domain.callog;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface CallLogRepository extends PagingAndSortingRepository<CallLog, Long> {

    Iterable<CallLog> findByVerboiceId(Long verboiceId);

}
