package com.cicd.coursemanager.controller;

import com.cicd.coursemanager.entity.Course;
import com.cicd.coursemanager.entity.Enrollment;
import com.cicd.coursemanager.service.CourseService;
import com.cicd.coursemanager.service.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final CourseService courseService;

    public EnrollmentController(EnrollmentService enrollmentService,
                                CourseService courseService) {
        this.enrollmentService = enrollmentService;
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<Enrollment>> getAll(
            @RequestParam(required = false) Long courseId) {
        if (courseId != null) {
            return ResponseEntity.ok(
                    enrollmentService.findByCourseId(courseId));
        }
        return ResponseEntity.ok(enrollmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enrollment> getById(@PathVariable Long id) {
        return enrollmentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> body) {
        Long courseId = Long.valueOf(body.get("courseId").toString());
        Optional<Course> course = courseService.findById(courseId);
        if (course.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Course not found"));
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentName(body.get("studentName").toString());
        enrollment.setStudentEmail(body.get("studentEmail").toString());
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setCourse(course.get());

        Enrollment saved = enrollmentService.save(enrollment);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        enrollmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
