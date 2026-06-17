package com.portfolio.controller;

import com.portfolio.client.MemberApiClient;
import com.portfolio.dto.MemberRequest;
import com.portfolio.dto.MemberResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberApiClient memberApiClient;

    public MemberController(MemberApiClient memberApiClient) {
        this.memberApiClient = memberApiClient;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse create(@Valid @RequestBody MemberRequest request) {
        return memberApiClient.create(request);
    }

    @GetMapping
    public List<MemberResponse> findAll() {
        return memberApiClient.findAll();
    }

    @GetMapping("/{id}")
    public MemberResponse findById(@PathVariable Long id) {
        return memberApiClient.findById(id);
    }
}
