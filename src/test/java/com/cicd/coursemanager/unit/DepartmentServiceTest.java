package com.cicd.coursemanager.unit;

import com.cicd.coursemanager.entity.Department;
import com.cicd.coursemanager.repository.DepartmentRepository;
import com.cicd.coursemanager.service.DepartmentService;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for Department entity and service.
 * These tests use Mockito — no Spring context, no database.
 */
@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department("Computer Science",
                "Study of computation");
        department.setId(1L);
    }

    @Test
    @DisplayName("Should create department with valid fields")
    void testCreateDepartment() {
        when(departmentRepository.save(any(Department.class)))
                .thenReturn(department);

        Department saved = departmentService.save(department);

        assertNotNull(saved);
        assertEquals("Computer Science", saved.getName());
        verify(departmentRepository, times(1)).save(department);
    }

    @Test
    @DisplayName("Should return all departments")
    void testFindAllDepartments() {
        Department dept2 = new Department("Mathematics",
                "Study of numbers");
        dept2.setId(2L);
        when(departmentRepository.findAll())
                .thenReturn(Arrays.asList(department, dept2));

        List<Department> result = departmentService.findAll();

        assertEquals(2, result.size());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find department by ID")
    void testFindDepartmentById() {
        when(departmentRepository.findById(1L))
                .thenReturn(Optional.of(department));

        Optional<Department> found = departmentService.findById(1L);

        assertTrue(found.isPresent());
        assertEquals("Computer Science", found.get().getName());
    }

    @Test
    @DisplayName("Should return empty when department not found")
    void testFindDepartmentByIdNotFound() {
        when(departmentRepository.findById(99L))
                .thenReturn(Optional.empty());

        Optional<Department> found = departmentService.findById(99L);

        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Should delete department by ID")
    void testDeleteDepartment() {
        departmentService.deleteById(1L);

        verify(departmentRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Department entity getters and setters work correctly")
    void testDepartmentGettersSetters() {
        Department dept = new Department();
        dept.setId(5L);
        dept.setName("Physics");
        dept.setDescription("Study of matter");

        assertEquals(5L, dept.getId());
        assertEquals("Physics", dept.getName());
        assertEquals("Study of matter", dept.getDescription());
    }

    @Test
    @DisplayName("Department courses list is initialized empty")
    void testDepartmentCoursesInitializedEmpty() {
        Department dept = new Department("Test", "Test Dept");

        assertNotNull(dept.getCourses());
        assertTrue(dept.getCourses().isEmpty());
    }
}