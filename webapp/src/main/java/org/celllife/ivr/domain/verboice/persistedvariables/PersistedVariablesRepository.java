package org.celllife.ivr.domain.verboice.persistedvariables;

import org.celllife.ivr.domain.verboice.projectvariables.ProjectVariables;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.persistence.QueryHint;

public interface PersistedVariablesRepository extends PagingAndSortingRepository<PersistedVariables,Integer> {

    @Query("select new org.celllife.ivr.domain.verboice.persistedvariables.PersistedVariables(p.value, p.createdAt, p.updatedAt, p.contactId, p.projectVariableId) from PersistedVariables p " +
            "where contactId= :contactId " +
            "and projectVariableId= :projectVariableId")
    Iterable<PersistedVariables> findByContactIdAndProjectVariableId(@Param("contactId") Integer contactId, @Param("projectVariableId") Integer projectVariableId);

    @Query("select new org.celllife.ivr.domain.verboice.persistedvariables.PersistedVariables(p.value, p.createdAt, p.updatedAt, p.contactId, p.projectVariableId) from PersistedVariables p " +
            "where value= :value ")
    Iterable<PersistedVariables> findByValue(@Param("value") String value);

    @Query("select count(*) from PersistedVariables p where value= :value")
    Long findTotalVariablesWithValue(@Param("value") String value);

}
