package com.cicd.coursemanager.unit;

import com.cicd.coursemanager.entity.Course;
import com.cicd.coursemanager.entity.Department;
import com.cicd.coursemanager.repository.CourseRepository;
import com.cicd.coursemanager.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for Course entity and service.
 * Uses Mockito — no Spring context, no database.
 */
@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    private Department department;
    private Course course;

    @BeforeEach
    void setUp() {
        department = new Department("Computer Science",
                "Study of computation");
        department.setId(1L);

        course = new Course("Java Programming", "CS101",
                "Intro to Java", 3, department);
        course.setId(1L);
    }

    @Test
    @DisplayName("Should save a new course")
    void testSaveCourse() {
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course saved = courseService.save(course);

        assertNotNull(saved);
        assertEquals("CS101", saved.getCode());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    @DisplayName("Should find all courses")
    void testFindAllCourses() {
        Course course2 = new Course("Data Structures", "CS201",
                "Advanced data structures", 4, department);
        course2.setId(2L);
        when(courseRepository.findAll())
                .thenReturn(Arrays.asList(course, course2));

        List<Course> result = courseService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Should find course by ID")
    void testFindCourseById() {
        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        Optional<Course> found = courseService.findById(1L);

        assertTrue(found.isPresent());
        assertEquals("Java Programming", found.get().getName());
    }

    @Test
    @DisplayName("Should find courses by department ID")
    void testFindByDepartmentId() {
        when(courseRepository.findByDepartmentId(1L))
                .thenReturn(List.of(course));

        List<Course> result = courseService.findByDepartmentId(1L);

        assertEquals(1, result.size());
        assertEquals("CS101", result.get(0).getCode());
    }

    @Test
    @DisplayName("Course entity getters and setters work correctly")
    void testCourseGettersSetters() {
        Course c = new Course();
        c.setId(10L);
        c.setName("Algorithms");
        c.setCode("CS301");
        c.setCredits(4);
        c.setDepartment(department);

        assertEquals(10L, c.getId());
        assertEquals("Algorithms", c.getName());
        assertEquals("CS301", c.getCode());
        assertEquals(4, c.getCredits());
        assertEquals(1L, c.getDepartmentId());
    }

    @Test
    @DisplayName("Course getDepartmentId returns null when no department")
    void testCourseDepartmentIdNull() {
        Course c = new Course();
        assertEquals(null, c.getDepartmentId());
    }
}