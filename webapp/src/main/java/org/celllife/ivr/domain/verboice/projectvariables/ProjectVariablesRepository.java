package org.celllife.ivr.domain.verboice.projectvariables;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.persistence.QueryHint;

public interface ProjectVariablesRepository extends PagingAndSortingRepository<ProjectVariables,Integer> {

    Iterable<ProjectVariables> findByName(String name);

    @Query("select new org.celllife.ivr.domain.verboice.projectvariables.ProjectVariables(p.id, p.projectId, p.name, p.createdAt, p.updatedAt)  " +
            "from ProjectVariables p " +
            "where p.projectId= :projectId " +
            "and p.name= :name")
    Iterable<ProjectVariables> findByNameAndProject(@Param("projectId") Integer projectId, @Param("name") String name);

}
