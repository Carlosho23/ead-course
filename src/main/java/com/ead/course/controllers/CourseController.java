package com.ead.course.controllers;

import com.ead.course.dtos.CourseRecordDto;
import com.ead.course.services.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {

    final CourseService courseService;

    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody @Valid CourseRecordDto courseRecordDto) {
        if (courseService.existsByName(courseRecordDto.name())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Course Name Already Taken!");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseRecordDto));
    }
}
