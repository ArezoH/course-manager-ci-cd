-- ============================================
-- Schema for test database (H2 compatible)
-- ============================================

DROP TABLE IF EXISTS enrollments;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS departments;

CREATE TABLE departments (
                             id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                             name        VARCHAR(100) NOT NULL UNIQUE,
                             description VARCHAR(255)
);

CREATE TABLE courses (
                         id            BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name          VARCHAR(100) NOT NULL,
                         code          VARCHAR(20)  NOT NULL UNIQUE,
                         description   VARCHAR(500),
                         credits       INTEGER      NOT NULL,
                         department_id BIGINT       NOT NULL,
                         CONSTRAINT fk_course_department
                             FOREIGN KEY (department_id) REFERENCES departments (id)
);

CREATE TABLE enrollments (
                             id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                             student_name    VARCHAR(100) NOT NULL,
                             student_email   VARCHAR(100) NOT NULL,
                             enrollment_date DATE         NOT NULL,
                             course_id       BIGINT       NOT NULL,
                             CONSTRAINT fk_enrollment_course
                                 FOREIGN KEY (course_id) REFERENCES courses (id)
);