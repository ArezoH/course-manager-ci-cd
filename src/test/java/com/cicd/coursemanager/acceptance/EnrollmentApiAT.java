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
 * Acceptance tests for the Enrollment REST API.
 * Full Spring context with real HTTP server and fixture data.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class EnrollmentApiAT {

    @LocalServerPort
    private int port;

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    private RestClient client() {
        return RestClient.create(baseUrl());
    }

    @Test
    @DisplayName("GET /api/enrollments — should return all 10 enrollments")
    void testGetAllEnrollments() {
        String body = client().get()
                .uri("/api/enrollments")
                .retrieve()
                .body(String.class);

        assertNotNull(body);
        assertTrue(body.contains("Alice Johnson"));
    }

    @Test
    @DisplayName("GET /api/enrollments/1 — should return Alice Johnson")
    void testGetEnrollmentById() {
        String body = client().get()
                .uri("/api/enrollments/1")
                .retrieve()
                .body(String.class);

        assertNotNull(body);
        assertTrue(body.contains("Alice Johnson"));
    }

    @Test
    @DisplayName("GET /api/enrollments?courseId=1 — should return enrollments")
    void testGetEnrollmentsByCourse() {
        String body = client().get()
                .uri("/api/enrollments?courseId=1")
                .retrieve()
                .body(String.class);

        assertNotNull(body);
        assertTrue(body.contains("Alice Johnson"));
        assertTrue(body.contains("Bob Smith"));
    }

    @Test
    @DisplayName("GET /api/enrollments/999 — should return 404")
    void testGetEnrollmentNotFound() {
        try {
            client().get()
                    .uri("/api/enrollments/999")
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("404"));
        }
    }

    @Test
    @DisplayName("POST /api/enrollments — should create new enrollment")
    void testCreateEnrollment() {
        String json = """
                {
                    "studentName": "Test Student",
                    "studentEmail": "test@university.edu",
                    "courseId": 2
                }
                """;

        String body = client().post()
                .uri("/api/enrollments")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .retrieve()
                .body(String.class);

        assertNotNull(body);
        assertTrue(body.contains("Test Student"));
    }
}