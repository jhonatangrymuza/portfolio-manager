package com.portfolio.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.portfolio.model.Member;

@DisplayName("MemberResponse - Conversão de entidade")
class MemberResponseTest {

    @Test
    @DisplayName("from() deve converter Member para MemberResponse")
    void from_shouldConvertMemberToResponse() {
        Member member = new Member();
        member.setId(1L);
        member.setName("João Silva");
        member.setRole("funcionário");

        MemberResponse response = MemberResponse.from(member);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("João Silva", response.name());
        assertEquals("funcionário", response.role());
    }
}
