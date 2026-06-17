package com.portfolio.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.dto.ProjectFilterRequest;
import com.portfolio.dto.ProjectRequest;
import com.portfolio.dto.ProjectResponse;
import com.portfolio.enums.ProjectStatus;
import com.portfolio.exception.BusinessException;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.model.Member;
import com.portfolio.model.Project;
import com.portfolio.model.ProjectMember;
import com.portfolio.repository.ProjectMemberRepository;
import com.portfolio.repository.ProjectRepository;
import com.portfolio.specification.ProjectSpecification;

@Service
@Transactional
public class ProjectService {

    private static final List<ProjectStatus> EXCLUDED_STATUSES =
            List.of(ProjectStatus.ENCERRADO, ProjectStatus.CANCELADO);
    private static final int MAX_MEMBERS_PER_PROJECT = 10;
    private static final int MAX_ACTIVE_PROJECTS_PER_MEMBER = 3;

    private final ProjectRepository projectRepository;
    private final MemberService memberService;
    private final ProjectMemberRepository projectMemberRepository;

    public ProjectService(ProjectRepository projectRepository,
                          MemberService memberService,
                          ProjectMemberRepository projectMemberRepository) {
        this.projectRepository = projectRepository;
        this.memberService = memberService;
        this.projectMemberRepository = projectMemberRepository;
    }

    public ProjectResponse create(ProjectRequest request) {
        Member manager = memberService.findEntityById(request.managerId());
        validateDates(request);

        Project project = new Project();
        mapRequestToProject(project, request, manager);
        return ProjectResponse.from(projectRepository.save(project));
    }

    @Transactional(readOnly = true)
    public Page<ProjectResponse> findAll(ProjectFilterRequest filter, Pageable pageable) {
        return projectRepository.findAll(ProjectSpecification.build(filter), pageable)
                .map(ProjectResponse::from);
    }

    @Transactional(readOnly = true)
    public List<Project> findAllWithDetails() {
        return projectRepository.findAllWithDetails();
    }

    @Transactional(readOnly = true)
    public long countDistinctAllocatedMembers() {
        return projectMemberRepository.countDistinctMembers();
    }

    @Transactional(readOnly = true)
    public ProjectResponse findById(Long id) {
        return projectRepository.findByIdWithDetails(id)
                .map(ProjectResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrado com id: " + id));
    }

    public ProjectResponse update(Long id, ProjectRequest request) {
        Project project = getProjectOrThrow(id);
        Member manager = memberService.findEntityById(request.managerId());
        validateDates(request);
        mapRequestToProject(project, request, manager);
        return ProjectResponse.from(projectRepository.save(project));
    }

    public void delete(Long id) {
        Project project = getProjectOrThrow(id);
        if (!project.getStatus().isDeletable()) {
            throw new BusinessException(
                    "Projeto com status '" + project.getStatus() + "' não pode ser excluído.");
        }
        projectRepository.delete(project);
    }

    public ProjectResponse updateStatus(Long id, ProjectStatus newStatus) {
        Project project = getProjectOrThrow(id);
        if (!project.getStatus().canTransitionTo(newStatus)) {
            throw new BusinessException(
                    "Transição de status inválida: " + project.getStatus() + " → " + newStatus);
        }
        project.setStatus(newStatus);
        return ProjectResponse.from(projectRepository.save(project));
    }

    public ProjectResponse addMember(Long projectId, Long memberId) {
        Project project = getProjectOrThrow(projectId);
        Member member = memberService.findEntityById(memberId);

        if (!"funcionário".equalsIgnoreCase(member.getRole())) {
            throw new BusinessException("Apenas membros com atribuição 'funcionário' podem ser associados a projetos.");
        }

        if (projectMemberRepository.findByProjectAndMember(project, member).isPresent()) {
            throw new BusinessException("Membro já está alocado neste projeto.");
        }

        long currentMemberCount = projectMemberRepository.countMembersByProject(project);
        if (currentMemberCount >= MAX_MEMBERS_PER_PROJECT) {
            throw new BusinessException("O projeto já atingiu o limite máximo de " + MAX_MEMBERS_PER_PROJECT + " membros.");
        }

        long activeProjectCount = projectMemberRepository.countActiveProjectsByMember(member, EXCLUDED_STATUSES);
        if (activeProjectCount >= MAX_ACTIVE_PROJECTS_PER_MEMBER) {
            throw new BusinessException(
                    "Membro já está alocado em 3 projetos ativos. Limite máximo atingido.");
        }

        ProjectMember pm = new ProjectMember();
        pm.setProject(project);
        pm.setMember(member);
        projectMemberRepository.save(pm);
        return projectRepository.findByIdWithDetails(projectId)
                .map(ProjectResponse::from)
                .orElseThrow();
    }

    public ProjectResponse removeMember(Long projectId, Long memberId) {
        Project project = getProjectOrThrow(projectId);
        Member member = memberService.findEntityById(memberId);

        ProjectMember pm = projectMemberRepository.findByProjectAndMember(project, member)
                .orElseThrow(() -> new ResourceNotFoundException("Membro não está alocado neste projeto."));

        long currentMemberCount = projectMemberRepository.countMembersByProject(project);
        if (currentMemberCount <= 1) {
            throw new BusinessException("O projeto deve ter no mínimo 1 membro alocado.");
        }

        projectMemberRepository.delete(pm);
        return projectRepository.findByIdWithDetails(projectId)
                .map(ProjectResponse::from)
                .orElseThrow();
    }

    private void validateDates(ProjectRequest request) {
        if (request.estimatedEndDate().isBefore(request.startDate())) {
            throw new BusinessException("A previsão de término não pode ser anterior à data de início.");
        }
        if (request.actualEndDate() != null && request.actualEndDate().isBefore(request.startDate())) {
            throw new BusinessException("A data real de término não pode ser anterior à data de início.");
        }
    }

    private void mapRequestToProject(Project project, ProjectRequest request, Member manager) {
        project.setName(request.name());
        project.setStartDate(request.startDate());
        project.setEstimatedEndDate(request.estimatedEndDate());
        project.setActualEndDate(request.actualEndDate());
        project.setTotalBudget(request.totalBudget());
        project.setDescription(request.description());
        project.setManager(manager);
    }

    private Project getProjectOrThrow(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrado com id: " + id));
    }

}
