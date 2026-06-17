package com.portfolio.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Member - Entidade de membro")
class MemberTest {

    @Test
    @DisplayName("deve criar Member com valores corretos")
    void shouldCreateWithValues() {
        Member member = new Member();
        member.setId(1L);
        member.setName("João");
        member.setRole("gerente");

        assertNotNull(member);
        assertEquals(1L, member.getId());
        assertEquals("João", member.getName());
        assertEquals("gerente", member.getRole());
    }

    @Test
    @DisplayName("equals e hashCode devem funcionar corretamente")
    void shouldSupportEqualsAndHashCode() {
        Member m1 = new Member();
        m1.setId(1L);
        m1.setName("A");
        m1.setRole("R");

        Member m2 = new Member();
        m2.setId(1L);
        m2.setName("A");
        m2.setRole("R");

        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    @DisplayName("toString deve retornar string não vazia")
    void shouldSupportToString() {
        Member member = new Member();
        member.setId(1L);
        member.setName("Teste");
        member.setRole("funcionário");

        String str = member.toString();
        assertNotNull(str);
        assertTrue(str.contains("Teste"));
    }

    @Test
    @DisplayName("construtor com todos os argumentos deve funcionar")
    void shouldSupportAllArgsConstructor() {
        Member member = new Member(1L, "Nome", "gerente");
        assertEquals("Nome", member.getName());
    }

    @Test
    @DisplayName("construtor sem argumentos deve funcionar")
    void shouldSupportNoArgsConstructor() {
        Member member = new Member();
        assertNotNull(member);
    }
}
