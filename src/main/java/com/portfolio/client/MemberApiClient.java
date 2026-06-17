package com.portfolio.client;

import com.portfolio.dto.MemberRequest;
import com.portfolio.dto.MemberResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class MemberApiClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public MemberApiClient(RestTemplate restTemplate,
                           @Value("${member.api.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public MemberResponse create(MemberRequest request) {
        return restTemplate.postForObject(baseUrl + "/api/mock/members", request, MemberResponse.class);
    }

    public List<MemberResponse> findAll() {
        ResponseEntity<List<MemberResponse>> response = restTemplate.exchange(
                baseUrl + "/api/mock/members",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        return response.getBody();
    }

    public MemberResponse findById(Long id) {
        return restTemplate.getForObject(baseUrl + "/api/mock/members/" + id, MemberResponse.class);
    }
}
