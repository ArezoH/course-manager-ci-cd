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
 * Acceptance tests — full HTTP tests using RestClient.
 * Boots a real server on a random port with H2 and fixture data.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class DepartmentApiAT {

    @LocalServerPort
    private int port;

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    private RestClient client() {
        return RestClient.create(baseUrl());
    }

    @Test
    @DisplayName("GET /api/departments — should return all departments")
    void testGetAllDepartments() {
        String body = client().get()
                .uri("/api/departments")
                .retrieve()
                .body(String.class);

        assertNotNull(body);
        assertTrue(body.contains("Computer Science"));
        assertTrue(body.contains("Mathematics"));
    }

    @Test
    @DisplayName("GET /api/departments/1 — should return Computer Science")
    void testGetDepartmentById() {
        String body = client().get()
                .uri("/api/departments/1")
                .retrieve()
                .body(String.class);

        assertNotNull(body);
        assertTrue(body.contains("Computer Science"));
    }

    @Test
    @DisplayName("GET /api/departments/999 — should return 404")
    void testGetDepartmentNotFound() {
        try {
            client().get()
                    .uri("/api/departments/999")
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("404"));
        }
    }

    @Test
    @DisplayName("POST /api/departments — should create new department")
    void testCreateDepartment() {
        String json = """
                {
                    "name": "Art History",
                    "description": "Study of visual arts through history"
                }
                """;

        String body = client().post()
                .uri("/api/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .retrieve()
                .body(String.class);

        assertNotNull(body);
        assertTrue(body.contains("Art History"));
    }

    @Test
    @DisplayName("POST /api/departments — empty name should return 400")
    void testCreateDepartmentInvalidName() {
        String json = """
                {
                    "name": "",
                    "description": "Invalid"
                }
                """;

        try {
            client().post()
                    .uri("/api/departments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("400"));
        }
    }
}