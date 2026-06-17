package com.portfolio.specification;

import com.portfolio.dto.ProjectFilterRequest;
import com.portfolio.enums.ProjectStatus;
import com.portfolio.model.Project;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ProjectSpecification {

    private ProjectSpecification() {}

    public static Specification<Project> build(ProjectFilterRequest filter) {
        return Specification
                .where(hasName(filter.name()))
                .and(hasStatus(filter.status()))
                .and(hasManager(filter.managerId()))
                .and(startDateFrom(filter.startDateFrom()))
                .and(startDateTo(filter.startDateTo()));
    }

    private static Specification<Project> hasName(String name) {
        return (root, query, cb) ->
                (name == null || name.isBlank()) ? null
                        : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    private static Specification<Project> hasStatus(ProjectStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    private static Specification<Project> hasManager(Long managerId) {
        return (root, query, cb) ->
                managerId == null ? null : cb.equal(root.get("manager").get("id"), managerId);
    }

    private static Specification<Project> startDateFrom(LocalDate from) {
        return (root, query, cb) ->
                from == null ? null : cb.greaterThanOrEqualTo(root.get("startDate"), from);
    }

    private static Specification<Project> startDateTo(LocalDate to) {
        return (root, query, cb) ->
                to == null ? null : cb.lessThanOrEqualTo(root.get("startDate"), to);
    }
}
