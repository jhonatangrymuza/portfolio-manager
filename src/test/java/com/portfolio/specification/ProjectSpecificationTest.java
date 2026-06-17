package com.portfolio.specification;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import com.portfolio.dto.ProjectFilterRequest;
import com.portfolio.enums.ProjectStatus;
import com.portfolio.model.Project;

import java.time.LocalDate;

@DisplayName("ProjectSpecification - Construção de specs JPA")
class ProjectSpecificationTest {

    @Test
    @DisplayName("build() com todos os filtros deve retornar Specification não nula")
    void build_withAllFilters_shouldReturnSpecification() {
        ProjectFilterRequest filter = new ProjectFilterRequest(
                "Teste", ProjectStatus.EM_ANALISE, 1L,
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)
        );

        Specification<Project> spec = ProjectSpecification.build(filter);

        assertNotNull(spec);
    }

    @Test
    @DisplayName("build() com filtros nulos deve retornar Specification não nula")
    void build_withNullFilters_shouldReturnSpecification() {
        ProjectFilterRequest filter = new ProjectFilterRequest(null, null, null, null, null);

        Specification<Project> spec = ProjectSpecification.build(filter);

        assertNotNull(spec);
    }

    @Test
    @DisplayName("build() com nome em branco deve ignorar filtro de nome")
    void build_withBlankName_shouldIgnoreNameFilter() {
        ProjectFilterRequest filter = new ProjectFilterRequest("   ", ProjectStatus.EM_ANALISE, null, null, null);

        Specification<Project> spec = ProjectSpecification.build(filter);

        assertNotNull(spec);
    }
}
