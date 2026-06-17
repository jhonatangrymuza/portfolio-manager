package com.portfolio.service;

import com.portfolio.dto.MemberRequest;
import com.portfolio.dto.MemberResponse;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.model.Member;
import com.portfolio.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse create(MemberRequest request) {
        Member member = new Member();
        member.setName(request.name());
        member.setRole(request.role());
        return MemberResponse.from(memberRepository.save(member));
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> findAll() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public MemberResponse findById(Long id) {
        return memberRepository.findById(id)
                .map(MemberResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Membro não encontrado com id: " + id));
    }

    @Transactional(readOnly = true)
    public Member findEntityById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membro não encontrado com id: " + id));
    }
}
