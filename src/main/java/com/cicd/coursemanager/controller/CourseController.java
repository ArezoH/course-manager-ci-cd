package com.cicd.coursemanager.controller;

import com.cicd.coursemanager.entity.Course;
import com.cicd.coursemanager.entity.Department;
import com.cicd.coursemanager.service.CourseService;
import com.cicd.coursemanager.service.DepartmentService;
import jakarta.validation.Valid;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final DepartmentService departmentService;

    public CourseController(CourseService courseService,
                            DepartmentService departmentService) {
        this.courseService = courseService;
        this.departmentService = departmentService;
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAll(
            @RequestParam(required = false) Long departmentId) {
        if (departmentId != null) {
            return ResponseEntity.ok(
                    courseService.findByDepartment_Id(departmentId));
        }
        return ResponseEntity.ok(courseService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getById(@PathVariable Long id) {
        return courseService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Map<String, Object> body) {
        Long departmentId = Long.valueOf(body.get("departmentId").toString());
        Optional<Department> dept = departmentService.findById(departmentId);
        if (dept.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Department not found"));
        }

        Course course = new Course();
        course.setName(body.get("name").toString());
        course.setCode(body.get("code").toString());
        course.setDescription(body.getOrDefault("description", "").toString());
        course.setCredits(Integer.valueOf(body.get("credits").toString()));
        course.setDepartment(dept.get());

        Course saved = courseService.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        courseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}