package com.portfolio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.portfolio.dto.MemberRequest;
import com.portfolio.dto.MemberResponse;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.model.Member;
import com.portfolio.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("MemberService - Operações com membros")
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member();
        member.setId(1L);
        member.setName("João Silva");
        member.setRole("gerente");
    }

    @Test
    @DisplayName("create() deve salvar e retornar MemberResponse")
    void create_shouldSaveAndReturnResponse() {
        MemberRequest request = new MemberRequest("João Silva", "gerente");
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        MemberResponse response = memberService.create(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("João Silva", response.name());
        assertEquals("gerente", response.role());
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("findAll() deve retornar lista de MemberResponse")
    void findAll_shouldReturnListOfResponses() {
        when(memberRepository.findAll()).thenReturn(List.of(member));

        List<MemberResponse> result = memberService.findAll();

        assertEquals(1, result.size());
        assertEquals("João Silva", result.get(0).name());
    }

    @Test
    @DisplayName("findById() deve retornar MemberResponse quando encontrado")
    void findById_shouldReturnResponse_whenFound() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        MemberResponse response = memberService.findById(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
    }

    @Test
    @DisplayName("findById() deve lançar ResourceNotFoundException quando não encontrado")
    void findById_shouldThrow_whenNotFound() {
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> memberService.findById(99L));
    }

    @Test
    @DisplayName("findEntityById() deve retornar Member quando encontrado")
    void findEntityById_shouldReturnMember_whenFound() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        Member result = memberService.findEntityById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("findEntityById() deve lançar ResourceNotFoundException quando não encontrado")
    void findEntityById_shouldThrow_whenNotFound() {
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> memberService.findEntityById(99L));
    }
}
