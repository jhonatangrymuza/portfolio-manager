package com.portfolio.repository;

import com.portfolio.enums.ProjectStatus;
import com.portfolio.model.Member;
import com.portfolio.model.Project;
import com.portfolio.model.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    List<ProjectMember> findByProject(Project project);

    Optional<ProjectMember> findByProjectAndMember(Project project, Member member);

    @Query("SELECT COUNT(pm) FROM ProjectMember pm WHERE pm.member = :member " +
           "AND pm.project.status NOT IN :excludedStatuses")
    long countActiveProjectsByMember(Member member, List<ProjectStatus> excludedStatuses);

    @Query("SELECT COUNT(pm) FROM ProjectMember pm WHERE pm.project = :project")
    long countMembersByProject(Project project);

    @Query("SELECT COUNT(DISTINCT pm.member.id) FROM ProjectMember pm")
    long countDistinctMembers();
}
