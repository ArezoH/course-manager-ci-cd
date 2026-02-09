# Course Manager — CI/CD Pipeline Project

**Student:** Arezo Halimi

**Course:** Container Deployment and Management (25/26)

**Phase 3:** Building a CI/CD Pipeline

---

## 1. Introduction and Project Structure

This is a headless Java REST application for managing academic courses, departments, and student enrollments. The application is built with Spring Boot 4.0.2, uses PostgreSQL as the database, and runs on an embedded Apache Tomcat server. All API endpoints return JSON.

The primary objective of this project is not the application itself, but the **CI/CD pipeline configuration** and the **Docker Compose environment setup**.

### Application Model

The application contains 3 entity classes mapped to 3 database tables:

| Entity       | Table          | Description                              |
|-------------|----------------|------------------------------------------|
| Department  | `departments`  | Academic departments                     |
| Course      | `courses`      | Courses offered (FK → departments)       |
| Enrollment  | `enrollments`  | Student enrollments (FK → courses)       |

Relationships: `Department (1) → (*) Course (1) → (*) Enrollment`

### Project File Structure

```
course-manager/
├── .github/workflows/
│   └── ci.yml                        # GitHub Actions CI pipeline
├── docker/
│   └── init.sql                      # PostgreSQL schema and seed data
├── src/
│   ├── main/
│   │   ├── java/com/cicd/coursemanager/
│   │   │   ├── controller/           # REST controllers (JSON endpoints)
│   │   │   ├── entity/               # JPA entities (3 tables)
│   │   │   ├── repository/           # Spring Data JPA repositories
│   │   │   ├── service/              # Service layer
│   │   │   └── CourseManagerApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── java/com/cicd/coursemanager/
│       │   ├── unit/                 # Unit tests (*Test.java)
│       │   ├── integration/          # Integration tests (*IT.java)
│       │   └── acceptance/           # Acceptance tests (*AT.java)
│       └── resources/
│           ├── application.properties
│           ├── schema.sql
│           └── data.sql
├── api-tests.http                    # HTTP client test requests
├── checkstyle.xml                    # Checkstyle linting configuration
├── docker-compose.yml                # Docker Compose services
├── Dockerfile                        # Multi-stage application build
├── pom.xml                           # Maven project configuration
├── .gitignore
└── README.md
```

### Technologies Used

| Component          | Technology                |
|-------------------|---------------------------|
| Language           | Java 17                   |
| Framework          | Spring Boot 4.0.2         |
| Application Server | Apache Tomcat (embedded)  |
| Database           | PostgreSQL 16             |
| Test Database      | H2 (in-memory)            |
| Build Tool         | Maven                     |
| Linting            | Checkstyle 10.21.4        |
| Testing            | JUnit 5, Mockito          |
| Containerization   | Docker, Docker Compose    |
| CI/CD              | GitHub Actions             |

---

## 2. Commands to Start the Environment and Load Records

### Prerequisites

- Java 17+
- Maven 3.9+
- Docker and Docker Compose

### Starting the Environment

```bash
# Build and start all services
docker compose up --build -d

# Verify services are running
docker compose ps

# View application logs
docker compose logs app

# View database logs
docker compose logs db
```

This starts two Docker Compose services:

| Service | Container            | Image                 | Port | Purpose            |
|---------|---------------------|-----------------------|------|--------------------|
| `db`    | `coursemanager-db`   | `postgres:16-alpine`  | 5432 | PostgreSQL database|
| `app`   | `coursemanager-app`  | Built from Dockerfile | 8080 | Spring Boot API    |

Both services communicate through a Docker bridge network (`app-network`). The application service depends on the database service and waits for its health check to pass before starting.

### Loading Test Data (Records)

Test data is loaded **automatically**. No manual commands are needed.

| Environment        |  Source                  | How It Loads                                      |
|--------------------|---------------------------------|---------------------------------------------------|
| Docker Compose     | `docker/init.sql`               | Runs via `/docker-entrypoint-initdb.d/` on first container start |
| Integration Tests  | `src/test/resources/data.sql`   | Loaded by Spring Boot into H2 on test startup     |
| Acceptance Tests   | `src/test/resources/data.sql`   | Same as integration tests                         |
| Unit Tests         | Not applicable                  | Unit tests use Mockito mocks, no database needed  |

Each table contains **10 Record records**:
- 10 departments (Computer Science, Mathematics, Physics, etc.)
- 10 courses (CS101, CS201, MATH101, etc.)
- 10 enrollments (Alice Johnson, Bob Smith, Charlie Brown, etc.)

### Stopping the Environment

```bash
docker compose down -v
```

---

## 3. Commands to Run the Tests

```bash
# Run unit tests only
mvn test

# Run all tests (unit + integration + acceptance)
mvn verify

# Run Checkstyle linting
mvn checkstyle:check
```

Test separation is handled by Maven plugins and file naming conventions:

| Test Type       | File Pattern | Maven Plugin | Command      |
|----------------|-------------|-------------|--------------|
| Unit tests      | `*Test.java` | Surefire    | `mvn test`   |
| Integration tests | `*IT.java` | Failsafe    | `mvn verify` |
| Acceptance tests  | `*AT.java` | Failsafe    | `mvn verify` |

The Surefire plugin is configured to **exclude** `*IT.java` and `*AT.java` files. The Failsafe plugin is configured to **include** only `*IT.java` and `*AT.java` files. This ensures clean separation between test phases.

---

## 4. Commands to Verify Endpoints (curl)

After starting the environment with `docker compose up --build -d`, the following curl commands verify the API:

### Departments

```bash
# List all departments
curl http://localhost:8080/api/departments

# Get department by ID
curl http://localhost:8080/api/departments/1

# Create a new department
curl -X POST http://localhost:8080/api/departments \
  -H "Content-Type: application/json" \
  -d '{"name": "Music", "description": "Study of sound and rhythm"}'

# Delete a department
curl -X DELETE http://localhost:8080/api/departments/11
```

### Courses

```bash
# List all courses
curl http://localhost:8080/api/courses

# Filter courses by department
curl "http://localhost:8080/api/courses?departmentId=1"

# Get course by ID
curl http://localhost:8080/api/courses/1

# Create a new course
curl -X POST http://localhost:8080/api/courses \
  -H "Content-Type: application/json" \
  -d '{"name": "AI Fundamentals", "code": "CS301", "description": "Intro to AI", "credits": 3, "departmentId": 1}'
```

### Enrollments

```bash
# List all enrollments
curl http://localhost:8080/api/enrollments

# Filter enrollments by course
curl "http://localhost:8080/api/enrollments?courseId=1"

# Get enrollment by ID
curl http://localhost:8080/api/enrollments/1

# Create a new enrollment
curl -X POST http://localhost:8080/api/enrollments \
  -H "Content-Type: application/json" \
  -d '{"studentName": "John Doe", "studentEmail": "john@uni.edu", "courseId": 1}'
```

### IntelliJ HTTP Client

An `api-tests.http` file is included at the project root with 15 pre-configured HTTP requests covering all endpoints (GET, POST, DELETE for each entity). To use it:

1. Start Docker Compose: `docker compose up --build -d`
2. Open `api-tests.http` in IntelliJ IDEA
3. Click the green play button next to any request

---

## 5. Tests: Explanation and List

The project contains **33 tests** distributed across unit, integration, and acceptance tests. All tests use JUnit 5.

### Unit Tests (17 tests)

Unit tests run without Spring context and without a database. They use Mockito to mock repository dependencies.

| Test Class                | Tests | Description                                              |
|--------------------------|-------|----------------------------------------------------------|
| `DepartmentServiceTest`   | 7     | Tests for save, findAll, findById, findById not found, delete, getters/setters, empty courses list |
| `CourseServiceTest`        | 7     | Tests for save, findAll, findById, findByDepartmentId, delete, getters/setters, null departmentId |
| `EnrollmentEntityTest`     | 4     | Tests for constructor with all fields, getters/setters, null courseId, default constructor |

### Integration Tests (15 tests)

Integration tests boot the full Spring context with an H2 in-memory database. Record data from `data.sql` is loaded automatically. The `@Transactional` annotation ensures each test rolls back its changes.

| Test Class                  | Tests | Description                                              |
|----------------------------|-------|----------------------------------------------------------|
| `DepartmentRepositoryIT`    | 5     | Record count verification (10 records), find by ID, save new record, find specific department, not found case |
| `CourseRepositoryIT`         | 6     | Record count verification, first course check, find by department ID, empty department filter, not found case, save new course |
| `EnrollmentRepositoryIT`     | 4     | Record count verification (10 records), first enrollment check, find by course ID, not found case |

### Acceptance Tests (16 tests)

Acceptance tests start a real embedded Tomcat server on a random port and send actual HTTP requests using Spring's `RestClient`. Record data is loaded into H2.

| Test Class              | Tests | Description                                              |
|------------------------|-------|----------------------------------------------------------|
| `DepartmentApiAT`       | 5     | GET all departments, GET by ID, GET 404 not found, POST create, POST validation error (400) |
| `CourseApiAT`            | 5     | GET all courses, GET by ID, GET filtered by department, GET 404 not found, POST create |
| `EnrollmentApiAT`        | 6     | GET all enrollments, GET by ID, GET filtered by course, GET 404 not found, POST create, POST invalid course (400) |

---

## 6. GitHub Actions CI Workflow

The CI pipeline is defined in `.github/workflows/ci.yml`. It triggers on pushes to `main` and `develop` branches, and on pull requests to `main`.

### Pipeline Structure

The pipeline consists of **3 sequential jobs** covering the **6 required CI stages**:

```
Job 1: build-and-lint ──────────────────────────────
  Stage 1: Configure Java environment (JDK 17)
  Stage 2: Code linting with Checkstyle (mvn checkstyle:check)
  Stage 3: Execute unit tests (mvn test)
           │
           ▼ (only runs if Job 1 passes)
Job 2: integration-tests ──────────────────────────
  Stage 4: Execute integration tests (mvn verify -DskipTests)
           │
           ▼ (only runs if Job 2 passes)
Job 3: docker-and-acceptance ──────────────────────
  Stage 5: Build Docker image (docker build)
  Stage 6: Execute acceptance tests (docker compose up + curl)
```

### Stage Details

| Stage | CI Requirement              | Implementation                              | Fails Build on Error |
|-------|-----------------------------|---------------------------------------------|---------------------|
| 1     | Java environment setup      | `actions/setup-java@v4` with JDK 17 Temurin | —                   |
| 2     | Code linting (Checkstyle)   | `mvn checkstyle:check -B`                   | Yes                 |
| 3     | Unit test execution         | `mvn test -B` (Surefire runs *Test.java)    | Yes                 |
| 4     | Integration test execution  | `mvn verify -DskipTests -B` (Failsafe runs *IT.java, *AT.java) | Yes |
| 5     | Docker image build          | `docker build -t course-manager:latest .`   | Yes                 |
| 6     | Acceptance test execution   | `docker compose up` + curl commands against live endpoints | Yes |

### Design Decisions

- **Sequential execution**: Each job depends on the previous one via the `needs:` keyword. If linting fails, no tests run. If tests fail, Docker is never built.
- **Fail-fast approach**: The cheapest operations (linting, unit tests) run first. The most expensive operation (Docker build) runs last.
- **Maven dependency caching**: The `setup-java` action is configured with `cache: maven` to speed up dependency resolution.
- **Health check loop**: The acceptance test stage polls `http://localhost:8080/api/departments` up to 30 times (5-second intervals) before running curl tests.
- **Cleanup**: `docker compose down -v` runs with `if: always()` to ensure containers are stopped even if tests fail.

---

## Docker Compose Configuration

### docker-compose.yml

The file defines two services:

**Database service (`db`):**
- Image: `postgres:16-alpine`
- Health check: `pg_isready -U postgres`
- Volume: `postgres_data` for data persistence
- Init script: `docker/init.sql` mounted to `/docker-entrypoint-initdb.d/`

**Application service (`app`):**
- Built from the project `Dockerfile` (multi-stage: Maven build → JRE runtime)
- Depends on `db` with `condition: service_healthy`
- Environment variables override `application.properties` for database connection

**Networking:**
- Both services join `app-network` (bridge driver)
- The application connects to PostgreSQL via the hostname `db` (Docker DNS resolution)

### Dockerfile

Multi-stage build to minimize the final image size:
- **Stage 1 (build):** Uses `maven:3.9-eclipse-temurin-17` to compile and package the application
- **Stage 2 (runtime):** Uses `eclipse-temurin:17-jre` and copies only the built JAR file