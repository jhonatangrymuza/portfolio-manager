package com.portfolio.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.dto.ProjectFilterRequest;
import com.portfolio.dto.ProjectRequest;
import com.portfolio.dto.ProjectResponse;
import com.portfolio.dto.StatusUpdateRequest;
import com.portfolio.enums.ProjectStatus;
import com.portfolio.service.ProjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Projetos", description = "Gerenciamento do ciclo de vida de projetos")
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse create(@Valid @RequestBody ProjectRequest request) {
        return projectService.create(request);
    }

    @Operation(summary = "Listar projetos", description = "Retorna projetos paginados com filtros opcionais")
    @GetMapping
    public Page<ProjectResponse> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) ProjectStatus status,
            @RequestParam(required = false) Long managerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateTo,
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        var filter = new ProjectFilterRequest(name, status, managerId, startDateFrom, startDateTo);
        return projectService.findAll(filter, pageable);
    }

    @GetMapping("/{id}")
    public ProjectResponse findById(@PathVariable Long id) {
        return projectService.findById(id);
    }

    @PutMapping("/{id}")
    public ProjectResponse update(@PathVariable Long id, @Valid @RequestBody ProjectRequest request) {
        return projectService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        projectService.delete(id);
    }

    @PatchMapping("/{id}/status")
    public ProjectResponse updateStatus(@PathVariable Long id,
                                        @Valid @RequestBody StatusUpdateRequest request) {
        return projectService.updateStatus(id, request.status());
    }

    @PostMapping("/{projectId}/members/{memberId}")
    public ProjectResponse addMember(@PathVariable Long projectId, @PathVariable Long memberId) {
        return projectService.addMember(projectId, memberId);
    }

    @DeleteMapping("/{projectId}/members/{memberId}")
    public ProjectResponse removeMember(@PathVariable Long projectId, @PathVariable Long memberId) {
        return projectService.removeMember(projectId, memberId);
    }
}
