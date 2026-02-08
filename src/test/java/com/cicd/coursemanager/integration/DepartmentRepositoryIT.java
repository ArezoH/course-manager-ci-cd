package com.cicd.coursemanager.integration;

import org.springframework.transaction.annotation.Transactional;
import com.cicd.coursemanager.entity.Department;
import com.cicd.coursemanager.repository.DepartmentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests — boots Spring context with H2 database.
 * Uses fixture data from data.sql (10 departments).
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DepartmentRepositoryIT {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    @DisplayName("Fixture: Should load 10 departments from data.sql")
    void testFixtureDataLoaded() {
        List<Department> departments = departmentRepository.findAll();
        assertEquals(10, departments.size());
    }

    @Test
    @DisplayName("Fixture: First department should be Computer Science")
    void testFirstDepartment() {
        Optional<Department> dept = departmentRepository.findById(1L);
        assertTrue(dept.isPresent());
        assertEquals("Computer Science", dept.get().getName());
    }

    @Test
    @DisplayName("Should save a new department to database")
    void testSaveNewDepartment() {
        Department dept = new Department("Music", "Study of sound and rhythm");
        Department saved = departmentRepository.save(dept);

        assertNotNull(saved.getId());
        assertEquals("Music", saved.getName());
    }

    @Test
    @DisplayName("Should find department by ID")
    void testFindById() {
        Optional<Department> dept = departmentRepository.findById(3L);
        assertTrue(dept.isPresent());
        assertEquals("Physics", dept.get().getName());
    }

    @Test
    @DisplayName("Should return empty for non-existent ID")
    void testFindByIdNotFound() {
        Optional<Department> dept = departmentRepository.findById(999L);
        assertFalse(dept.isPresent());
    }
}