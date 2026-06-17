package com.portfolio.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ProjectMember - Entidade de associação")
class ProjectMemberTest {

    @Test
    @DisplayName("deve criar ProjectMember com projeto e membro")
    void shouldCreateWithProjectAndMember() {
        Project project = new Project();
        project.setId(1L);

        Member member = new Member();
        member.setId(2L);

        ProjectMember pm = new ProjectMember();
        pm.setId(10L);
        pm.setProject(project);
        pm.setMember(member);

        assertNotNull(pm);
        assertEquals(10L, pm.getId());
        assertEquals(1L, pm.getProject().getId());
        assertEquals(2L, pm.getMember().getId());
    }

    @Test
    @DisplayName("equals e hashCode devem funcionar corretamente")
    void shouldSupportEqualsAndHashCode() {
        Project p = new Project();
        p.setId(1L);
        Member m = new Member();
        m.setId(2L);

        ProjectMember pm1 = new ProjectMember();
        pm1.setId(1L);
        pm1.setProject(p);
        pm1.setMember(m);

        ProjectMember pm2 = new ProjectMember();
        pm2.setId(1L);
        pm2.setProject(p);
        pm2.setMember(m);

        assertEquals(pm1, pm2);
        assertEquals(pm1.hashCode(), pm2.hashCode());
    }

    @Test
    @DisplayName("toString deve retornar string não vazia")
    void shouldSupportToString() {
        ProjectMember pm = new ProjectMember();
        pm.setId(1L);

        String str = pm.toString();
        assertNotNull(str);
        assertTrue(str.contains("ProjectMember"));
    }

    @Test
    @DisplayName("construtor com todos os argumentos deve funcionar")
    void shouldSupportAllArgsConstructor() {
        Project p = new Project();
        Member m = new Member();
        ProjectMember pm = new ProjectMember(1L, p, m);
        assertNotNull(pm);
    }

    @Test
    @DisplayName("construtor sem argumentos deve funcionar")
    void shouldSupportNoArgsConstructor() {
        ProjectMember pm = new ProjectMember();
        assertNotNull(pm);
    }
}
