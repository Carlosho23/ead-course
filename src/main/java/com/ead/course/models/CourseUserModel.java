package com.ead.course.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Getter
@Setter
@Table(name = "TB_COURSES_USERS")
public class CourseUserModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public CourseUserModel(UUID id, UUID userId, CourseModel course) {
        this.id = id;
        this.userId = userId;
        this.course = course;
    }

    public CourseUserModel() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "course_id") // EX: Pode ser definido assim o nome da coluna
    private CourseModel course;


}
