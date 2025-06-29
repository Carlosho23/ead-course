package com.ead.course.clients;

import com.ead.course.dtos.CourseUserRecordDto;
import com.ead.course.dtos.ResponsePageDto;
import com.ead.course.dtos.UserRecordDto;
import com.ead.course.exceptions.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;

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
                    .body(new ParameterizedTypeReference<ResponsePageDto<UserRecordDto>>() {
                    });
        } catch (RestClientException e) {
            log.error("Error Request RestClient with users: {} ", e.getMessage());
            throw new RuntimeException("Error Request RestClient: ", e);
        }
    }

    public ResponseEntity<UserRecordDto> getOneUserById(UUID userId) {
        String url = baseUrlAuthuser + "/users/" + userId;

        return restClient.get()
                .uri(url)
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, response) -> {
                    log.error("Error: User not found; {} ", userId);
                    throw new NotFoundException("Error: User not found");
                })
                .toEntity(UserRecordDto.class);
    }

    public void postSubscriptionUserInCourse(UUID courseId, UUID userId) {
        String url = baseUrlAuthuser + "/users/" + userId + "/courses/subscription";
        log.debug("Request URL: {}", url);

        try {
            var courseUserRecordDto = new CourseUserRecordDto(courseId, userId);
            restClient.post()
                    .uri(url)
                    .contentType(APPLICATION_JSON)
                    .body(courseUserRecordDto)
                    .retrieve()
                    .toBodilessEntity();

        } catch (RestClientException e) {
            log.error("Error Request POST RestClient with cause: {} ", e.getMessage());
            throw new RuntimeException("Error Request POST RestClient: ", e);
        }
    }

    public void deleteCourseUserInAuthUser(UUID courseId) {
        String url = baseUrlAuthuser + "/users/courses/" + courseId;
        log.debug("Request URL: {}", url);

        try {
            restClient.delete()
                    .uri(url)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            log.error("Error Request DELETE RestClient with cause: {} ", e.getMessage());
            throw new RuntimeException("Error Request DELETE RestClient: ", e);
        }
    }

}
