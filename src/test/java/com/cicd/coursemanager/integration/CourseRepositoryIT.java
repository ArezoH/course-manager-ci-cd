package com.cicd.coursemanager.integration;

import com.cicd.coursemanager.entity.Course;
import com.cicd.coursemanager.repository.CourseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests — boots Spring context with H2 database.
 * Uses fixture data from data.sql (10 courses).
 */
@SpringBootTest
@ActiveProfiles("test")
class CourseRepositoryIT {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    @DisplayName("Fixture: Should load 10 courses from data.sql")
    void testFixtureCoursesLoaded() {
        List<Course> courses = courseRepository.findAll();
        assertEquals(10, courses.size());
    }

    @Test
    @DisplayName("Fixture: First course should be Introduction to Java")
    void testFirstCourse() {
        Optional<Course> course = courseRepository.findById(1L);
        assertTrue(course.isPresent());
        assertEquals("Introduction to Java", course.get().getName());
        assertEquals("CS101", course.get().getCode());
    }

    @Test
    @DisplayName("Should find courses by department ID")
    void testFindByDepartmentId() {
        List<Course> csCourses = courseRepository.findByDepartment_Id(1L);
        assertEquals(2, csCourses.size());
    }

    @Test
    @DisplayName("Should return empty list for department with no courses")
    void testFindByDepartmentIdEmpty() {
        List<Course> courses = courseRepository.findByDepartment_Id(9L);
        assertEquals(0, courses.size());
    }

    @Test
    @DisplayName("Should return empty for non-existent course ID")
    void testFindByIdNotFound() {
        Optional<Course> course = courseRepository.findById(999L);
        assertFalse(course.isPresent());
    }
}