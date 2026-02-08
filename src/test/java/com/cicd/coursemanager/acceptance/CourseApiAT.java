package com.cicd.coursemanager.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Acceptance tests for the Course REST API.
 * Full Spring context with real HTTP server and fixture data.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CourseApiAT {

    @LocalServerPort
    private int port;

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    private RestClient client() {
        return RestClient.create(baseUrl());
    }

    @Test
    @DisplayName("GET /api/courses — should return all 10 courses")
    void testGetAllCourses() {
        String body = client().get()
                .uri("/api/courses")
                .retrieve()
                .body(String.class);

        assertNotNull(body);
        assertTrue(body.contains("Introduction to Java"));
        assertTrue(body.contains("Data Structures"));
    }

    @Test
    @DisplayName("GET /api/courses/1 — should return Introduction to Java")
    void testGetCourseById() {
        String body = client().get()
                .uri("/api/courses/1")
                .retrieve()
                .body(String.class);

        assertNotNull(body);
        assertTrue(body.contains("CS101"));
    }

    @Test
    @DisplayName("GET /api/courses?departmentId=1 — should return CS courses")
    void testGetCoursesByDepartment() {
        String body = client().get()
                .uri("/api/courses?departmentId=1")
                .retrieve()
                .body(String.class);

        assertNotNull(body);
        assertTrue(body.contains("CS101"));
        assertTrue(body.contains("CS201"));
    }

    @Test
    @DisplayName("GET /api/courses/999 — should return 404")
    void testGetCourseNotFound() {
        try {
            client().get()
                    .uri("/api/courses/999")
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("404"));
        }
    }

    @Test
    @DisplayName("POST /api/courses — should create a new course")
    void testCreateCourse() {
        String json = """
                {
                    "name": "Machine Learning",
                    "code": "CS401",
                    "description": "Introduction to ML",
                    "credits": 4,
                    "departmentId": 1
                }
                """;

        String body = client().post()
                .uri("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .retrieve()
                .body(String.class);

        assertNotNull(body);
        assertTrue(body.contains("Machine Learning"));
    }
}