package com.portfolio.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.portfolio.dto.ProjectRequest;
import com.portfolio.enums.ProjectStatus;
import com.portfolio.exception.BusinessException;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.model.Member;
import com.portfolio.model.Project;
import com.portfolio.model.ProjectMember;
import com.portfolio.repository.ProjectMemberRepository;
import com.portfolio.repository.ProjectRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProjectService - Regras de negócio")
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @InjectMocks
    private ProjectService projectService;

    private Member manager;
    private Member funcionario;

    @BeforeEach
    void setUp() {
        manager = new Member();
        manager.setId(1L);
        manager.setName("Manager");
        manager.setRole("gerente");

        funcionario = new Member();
        funcionario.setId(2L);
        funcionario.setName("Funcionário");
        funcionario.setRole("funcionário");
    }

    private ProjectMember buildProjectMember(Project project, Member member) {
        ProjectMember pm = new ProjectMember();
        pm.setProject(project);
        pm.setMember(member);
        return pm;
    }

    private Project buildProject(Long id, ProjectStatus status) {
        Project project = new Project();
        project.setId(id);
        project.setName("Projeto Teste");
        project.setStartDate(LocalDate.of(2024, 1, 1));
        project.setEstimatedEndDate(LocalDate.of(2024, 4, 1));
        project.setTotalBudget(new BigDecimal("50000"));
        project.setManager(manager);
        project.setStatus(status);
        return project;
    }

    // =========================================================
    // delete()
    // =========================================================

    @ParameterizedTest
    @EnumSource(value = ProjectStatus.class, names = {"INICIADO", "EM_ANDAMENTO", "ENCERRADO"})
    @DisplayName("delete() deve lançar BusinessException para status que proíbem exclusão")
    void delete_shouldThrow_whenStatusForbidsDeletion(ProjectStatus status) {
        Project project = buildProject(1L, status);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        assertThrows(BusinessException.class, () -> projectService.delete(1L));
        verify(projectRepository, never()).delete(any(Project.class));
    }

    @ParameterizedTest
    @EnumSource(value = ProjectStatus.class,
            names = {"EM_ANALISE", "ANALISE_REALIZADA", "ANALISE_APROVADA", "PLANEJADO", "CANCELADO"})
    @DisplayName("delete() deve ter sucesso para status que permitem exclusão")
    void delete_shouldSucceed_whenStatusAllowsDeletion(ProjectStatus status) {
        Project project = buildProject(1L, status);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        assertDoesNotThrow(() -> projectService.delete(1L));
        verify(projectRepository).delete(project);
    }

    @Test
    @DisplayName("delete() deve lançar ResourceNotFoundException quando projeto não existe")
    void delete_shouldThrow_whenProjectNotFound() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> projectService.delete(99L));
    }

    // =========================================================
    // updateStatus()
    // =========================================================

    @Test
    @DisplayName("updateStatus() deve lançar BusinessException para transição inválida")
    void updateStatus_shouldThrow_whenTransitionIsInvalid() {
        Project project = buildProject(1L, ProjectStatus.EM_ANALISE);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        assertThrows(BusinessException.class,
                () -> projectService.updateStatus(1L, ProjectStatus.INICIADO));
    }

    @Test
    @DisplayName("updateStatus() deve ter sucesso para transição sequencial válida")
    void updateStatus_shouldSucceed_whenTransitionIsValid() {
        Project project = buildProject(1L, ProjectStatus.EM_ANALISE);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepository.save(any())).thenReturn(project);

        assertDoesNotThrow(() -> projectService.updateStatus(1L, ProjectStatus.ANALISE_REALIZADA));
        assertEquals(ProjectStatus.ANALISE_REALIZADA, project.getStatus());
    }

    @Test
    @DisplayName("updateStatus() deve permitir cancelamento de qualquer status não-terminal")
    void updateStatus_shouldAllowCancellation_fromNonTerminalStatus() {
        Project project = buildProject(1L, ProjectStatus.EM_ANDAMENTO);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepository.save(any())).thenReturn(project);

        assertDoesNotThrow(() -> projectService.updateStatus(1L, ProjectStatus.CANCELADO));
    }

    @Test
    @DisplayName("updateStatus() deve lançar BusinessException ao tentar cancelar projeto encerrado")
    void updateStatus_shouldThrow_whenCancellingEncerrado() {
        Project project = buildProject(1L, ProjectStatus.ENCERRADO);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        assertThrows(BusinessException.class,
                () -> projectService.updateStatus(1L, ProjectStatus.CANCELADO));
    }

    // =========================================================
    // addMember()
    // =========================================================

    @Test
    @DisplayName("addMember() deve lançar BusinessException quando membro não é funcionário")
    void addMember_shouldThrow_whenMemberIsNotFuncionario() {
        Project project = buildProject(1L, ProjectStatus.EM_ANALISE);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(memberService.findEntityById(1L)).thenReturn(manager);

        assertThrows(BusinessException.class, () -> projectService.addMember(1L, 1L));
    }

    @Test
    @DisplayName("addMember() deve lançar BusinessException quando membro já está alocado")
    void addMember_shouldThrow_whenMemberAlreadyAllocated() {
        Project project = buildProject(1L, ProjectStatus.EM_ANALISE);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(memberService.findEntityById(2L)).thenReturn(funcionario);
        when(projectMemberRepository.findByProjectAndMember(project, funcionario))
                .thenReturn(Optional.of(buildProjectMember(project, funcionario)));

        assertThrows(BusinessException.class, () -> projectService.addMember(1L, 2L));
    }

    @Test
    @DisplayName("addMember() deve lançar BusinessException quando projeto atingiu 10 membros")
    void addMember_shouldThrow_whenProjectIsAtCapacity() {
        Project project = buildProject(1L, ProjectStatus.EM_ANALISE);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(memberService.findEntityById(2L)).thenReturn(funcionario);
        when(projectMemberRepository.findByProjectAndMember(project, funcionario))
                .thenReturn(Optional.empty());
        when(projectMemberRepository.countMembersByProject(project)).thenReturn(10L);

        assertThrows(BusinessException.class, () -> projectService.addMember(1L, 2L));
    }

    @Test
    @DisplayName("addMember() deve lançar BusinessException quando membro tem 3 projetos ativos")
    void addMember_shouldThrow_whenMemberHasThreeActiveProjects() {
        Project project = buildProject(1L, ProjectStatus.EM_ANALISE);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(memberService.findEntityById(2L)).thenReturn(funcionario);
        when(projectMemberRepository.findByProjectAndMember(project, funcionario))
                .thenReturn(Optional.empty());
        when(projectMemberRepository.countMembersByProject(project)).thenReturn(3L);
        when(projectMemberRepository.countActiveProjectsByMember(eq(funcionario), anyList()))
                .thenReturn(3L);

        assertThrows(BusinessException.class, () -> projectService.addMember(1L, 2L));
    }

    @Test
    @DisplayName("addMember() deve ter sucesso quando todas as condições são atendidas")
    void addMember_shouldSucceed_whenAllConditionsAreMet() {
        Project project = buildProject(1L, ProjectStatus.EM_ANALISE);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(memberService.findEntityById(2L)).thenReturn(funcionario);
        when(projectMemberRepository.findByProjectAndMember(project, funcionario))
                .thenReturn(Optional.empty());
        when(projectMemberRepository.countMembersByProject(project)).thenReturn(3L);
        when(projectMemberRepository.countActiveProjectsByMember(eq(funcionario), anyList()))
                .thenReturn(1L);
        when(projectMemberRepository.save(any())).thenReturn(buildProjectMember(project, funcionario));
        when(projectRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(project));

        assertDoesNotThrow(() -> projectService.addMember(1L, 2L));
        verify(projectMemberRepository).save(any(ProjectMember.class));
    }

    // =========================================================
    // removeMember()
    // =========================================================

    @Test
    @DisplayName("removeMember() deve lançar BusinessException ao remover o último membro")
    void removeMember_shouldThrow_whenRemovingLastMember() {
        Project project = buildProject(1L, ProjectStatus.EM_ANDAMENTO);
        ProjectMember pm = buildProjectMember(project, funcionario);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(memberService.findEntityById(2L)).thenReturn(funcionario);
        when(projectMemberRepository.findByProjectAndMember(project, funcionario))
                .thenReturn(Optional.of(pm));
        when(projectMemberRepository.countMembersByProject(project)).thenReturn(1L);

        assertThrows(BusinessException.class, () -> projectService.removeMember(1L, 2L));
        verify(projectMemberRepository, never()).delete(any(ProjectMember.class));
    }

    @Test
    @DisplayName("removeMember() deve ter sucesso quando há mais de 1 membro")
    void removeMember_shouldSucceed_whenMoreThanOneMember() {
        Project project = buildProject(1L, ProjectStatus.EM_ANDAMENTO);
        ProjectMember pm = buildProjectMember(project, funcionario);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(memberService.findEntityById(2L)).thenReturn(funcionario);
        when(projectMemberRepository.findByProjectAndMember(project, funcionario))
                .thenReturn(Optional.of(pm));
        when(projectMemberRepository.countMembersByProject(project)).thenReturn(3L);
        when(projectRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(project));

        assertDoesNotThrow(() -> projectService.removeMember(1L, 2L));
        verify(projectMemberRepository).delete(pm);
    }

    // =========================================================
    // create()
    // =========================================================

    @Test
    @DisplayName("create() deve lançar BusinessException quando estimatedEndDate < startDate")
    void create_shouldThrow_whenEstimatedEndDateIsBeforeStartDate() {
        ProjectRequest request = new ProjectRequest(
                "Projeto",
                LocalDate.of(2024, 6, 1),
                LocalDate.of(2024, 1, 1),
                null,
                new BigDecimal("50000"),
                "Desc",
                1L
        );
        when(memberService.findEntityById(1L)).thenReturn(manager);

        assertThrows(BusinessException.class, () -> projectService.create(request));
    }

    @Test
    @DisplayName("create() deve ter sucesso com datas válidas")
    void create_shouldSucceed_whenDatesAreValid() {
        ProjectRequest request = new ProjectRequest(
                "Projeto",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 6, 1),
                null,
                new BigDecimal("50000"),
                "Desc",
                1L
        );
        Project saved = buildProject(1L, ProjectStatus.EM_ANALISE);
        when(memberService.findEntityById(1L)).thenReturn(manager);
        when(projectRepository.save(any())).thenReturn(saved);

        assertDoesNotThrow(() -> projectService.create(request));
        verify(projectRepository).save(any(Project.class));
    }
}
