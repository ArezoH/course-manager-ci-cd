package com.cicd.coursemanager.integration;

import com.cicd.coursemanager.entity.Enrollment;
import com.cicd.coursemanager.repository.EnrollmentRepository;
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
 * Uses fixture data from data.sql (10 enrollments).
 */
@SpringBootTest
@ActiveProfiles("test")
class EnrollmentRepositoryIT {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Test
    @DisplayName("Fixture: Should load 10 enrollments from data.sql")
    void testFixtureEnrollmentsLoaded() {
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        assertEquals(10, enrollments.size());
    }

    @Test
    @DisplayName("Fixture: First enrollment should be Alice Johnson")
    void testFirstEnrollment() {
        Optional<Enrollment> enrollment = enrollmentRepository.findById(1L);
        assertTrue(enrollment.isPresent());
        assertEquals("Alice Johnson", enrollment.get().getStudentName());
    }

    @Test
    @DisplayName("Should find enrollments by course ID")
    void testFindByCourseId() {
        List<Enrollment> enrollments =
                enrollmentRepository.findByCourse_Id(1L);
        assertEquals(2, enrollments.size());
    }

    @Test
    @DisplayName("Should return empty for non-existent enrollment ID")
    void testFindByIdNotFound() {
        Optional<Enrollment> enrollment =
                enrollmentRepository.findById(999L);
        assertFalse(enrollment.isPresent());
    }
}