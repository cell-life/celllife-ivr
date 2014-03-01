package org.celllife.ivr.domain;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface CallLogRepository extends PagingAndSortingRepository<CallLog, String> {

}
