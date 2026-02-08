package com.cicd.coursemanager.unit;

import com.cicd.coursemanager.entity.Course;
import com.cicd.coursemanager.entity.Department;
import com.cicd.coursemanager.entity.Enrollment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for Enrollment entity.
 * Pure POJO tests — no Spring context, no database.
 */
class EnrollmentEntityTest {

    @Test
    @DisplayName("Should create enrollment with all fields")
    void testCreateEnrollment() {
        Department dept = new Department("CS", "Computer Science");
        Course course = new Course("Java", "CS101", "Intro", 3, dept);
        Enrollment enrollment = new Enrollment(
                "Alice Johnson",
                "alice@university.edu",
                LocalDate.of(2025, 1, 15),
                course
        );

        assertEquals("Alice Johnson", enrollment.getStudentName());
        assertEquals("alice@university.edu", enrollment.getStudentEmail());
        assertEquals(LocalDate.of(2025, 1, 15), enrollment.getEnrollmentDate());
        assertNotNull(enrollment.getCourse());
    }

    @Test
    @DisplayName("Enrollment getters and setters work correctly")
    void testEnrollmentGettersSetters() {
        Enrollment enrollment = new Enrollment();
        enrollment.setId(1L);
        enrollment.setStudentName("Bob Smith");
        enrollment.setStudentEmail("bob@university.edu");
        enrollment.setEnrollmentDate(LocalDate.of(2025, 2, 1));

        assertEquals(1L, enrollment.getId());
        assertEquals("Bob Smith", enrollment.getStudentName());
        assertEquals("bob@university.edu", enrollment.getStudentEmail());
        assertEquals(LocalDate.of(2025, 2, 1), enrollment.getEnrollmentDate());
    }

    @Test
    @DisplayName("Enrollment getCourseId returns null when no course")
    void testEnrollmentCourseIdNull() {
        Enrollment enrollment = new Enrollment();
        assertNull(enrollment.getCourseId());
    }
}