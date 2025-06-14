package com.ead.course.dtos;

import com.ead.course.enums.UserStatus;
import com.ead.course.enums.UserType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true) // Quando tiver valores desconhecidos ele vai ignorar
@JsonInclude(JsonInclude.Include.NON_NULL) // inclue valores que não sejam nulos
public record UserRecordDto(UUID userId,
                            String username,
                            String email,
                            String fullName,
                            UserStatus userStatus,
                            UserType userType,
                            String phoneNumber,
                            String imageUrl) {
}
