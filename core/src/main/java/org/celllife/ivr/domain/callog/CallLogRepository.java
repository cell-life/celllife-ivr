package org.celllife.ivr.domain.callog;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;

public interface CallLogRepository extends PagingAndSortingRepository<CallLog, Long> {

    Iterable<CallLog> findByVerboiceId(Long verboiceId);

    Iterable<CallLog> findByVerboiceProjectIdAndMsisdnAndMessageNumberOrderByDateDesc(Integer verboiceProjectId, String msisdn, Integer messageNumber);

    Iterable<CallLog> findByStateAndRetryDoneAndDateGreaterThan(String state, Boolean retryDone, Date date);
}
