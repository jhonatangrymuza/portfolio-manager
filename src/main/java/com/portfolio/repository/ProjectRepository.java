package com.portfolio.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.portfolio.enums.ProjectStatus;
import com.portfolio.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {

    @EntityGraph(attributePaths = {"manager"})
    Page<Project> findAll(Specification<Project> spec, Pageable pageable);

    List<Project> findByStatus(ProjectStatus status);

    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.projectMembers pm LEFT JOIN FETCH pm.member LEFT JOIN FETCH p.manager")
    List<Project> findAllWithDetails();

    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.projectMembers pm LEFT JOIN FETCH pm.member LEFT JOIN FETCH p.manager WHERE p.id = :id")
    java.util.Optional<Project> findByIdWithDetails(Long id);
}
