package com.ead.course.clients;

import com.ead.course.dtos.ResponsePageDto;
import com.ead.course.dtos.UserRecordDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

@Component
@Log4j2
public class AuthUserClient {

    @Value("${ead.api.url.authuser}")
    String baseUrlAuthuser;

    final RestClient restClient;

    public AuthUserClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public Page<UserRecordDto> getAllUsersByCourse(UUID courseId, Pageable pageable) {
        String url = baseUrlAuthuser + "/users?courseId=" + courseId + "&page=" +
                pageable.getPageNumber() + "&size=" + pageable.getPageSize() +
                "&sort=" + pageable.getSort().toString().replaceAll(": ", ",");

        try {


            return restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(new ParameterizedTypeReference<ResponsePageDto<UserRecordDto>>() {});
        } catch (RestClientException e) {
            log.error("Error Request RestClient with users: {} ", e.getMessage());
            throw new RuntimeException("Error Request RestClient: ", e);
        }
    }

}
