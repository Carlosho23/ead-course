package com.ead.course.services.impl;

import com.ead.course.dtos.LessonRecordDto;
import com.ead.course.dtos.ModuleRecordDto;
import com.ead.course.exceptions.NotFoundException;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.services.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    final LessonRepository lessonRepository;

    @Override
    public LessonModel save(LessonRecordDto lessonRecordDto, ModuleModel moduleModel) {
        var lessonModel = new LessonModel();
        BeanUtils.copyProperties(lessonRecordDto, lessonModel);
        lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        lessonModel.setModule(moduleModel);

        return lessonRepository.save(lessonModel);
    }

    @Override
    public List<LessonModel> findAllLessonsIntoModule(UUID moduleId) {
        return lessonRepository.findAllLessonsIntoModule(moduleId);
    }

    @Override
    public Optional<LessonModel> findLessonIntoModule(UUID lessonId, UUID moduleId) {
        Optional<LessonModel> lessonModelOptional = lessonRepository.findLessonIntoModule(moduleId, lessonId);
        if (lessonModelOptional.isEmpty()) {
            throw new NotFoundException("Error: Lesson not found for this Module.");
        }

        return lessonModelOptional;
    }

    @Override
    public void delete(LessonModel lessonModel) {
        lessonRepository.delete(lessonModel);
    }

    @Override
    public LessonModel update(LessonRecordDto lessonRecordDto, LessonModel lessonModel) {
        BeanUtils.copyProperties(lessonRecordDto, lessonModel);
        return lessonRepository.save(lessonModel);
    }

    @Override
    public Page<LessonModel> findAllLessonsIntoModule(Specification<LessonModel> spec, Pageable pageable) {
        return lessonRepository.findAll(spec, pageable);
    }

}
