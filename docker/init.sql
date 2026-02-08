-- ============================================
-- PostgreSQL Initialization Script
-- Runs automatically on first container start
-- ============================================

-- Create tables
CREATE TABLE IF NOT EXISTS departments (
                                           id          BIGSERIAL PRIMARY KEY,
                                           name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS courses (
                                       id            BIGSERIAL PRIMARY KEY,
                                       name          VARCHAR(100) NOT NULL,
    code          VARCHAR(20)  NOT NULL UNIQUE,
    description   VARCHAR(500),
    credits       INTEGER      NOT NULL,
    department_id BIGINT       NOT NULL,
    CONSTRAINT fk_course_department
    FOREIGN KEY (department_id) REFERENCES departments (id)
    );

CREATE TABLE IF NOT EXISTS enrollments (
                                           id              BIGSERIAL PRIMARY KEY,
                                           student_name    VARCHAR(100) NOT NULL,
    student_email   VARCHAR(100) NOT NULL,
    enrollment_date DATE         NOT NULL,
    course_id       BIGINT       NOT NULL,
    CONSTRAINT fk_enrollment_course
    FOREIGN KEY (course_id) REFERENCES courses (id)
    );

-- 10 Departments
INSERT INTO departments (name, description) VALUES
                                                ('Computer Science', 'Study of computation and information processing'),
                                                ('Mathematics', 'Study of numbers, quantities, and shapes'),
                                                ('Physics', 'Study of matter, energy, and fundamental forces'),
                                                ('Chemistry', 'Study of substances and their interactions'),
                                                ('Biology', 'Study of living organisms'),
                                                ('Engineering', 'Application of science to design and build'),
                                                ('Economics', 'Study of production and distribution of goods'),
                                                ('Philosophy', 'Study of fundamental nature of knowledge'),
                                                ('History', 'Study of past events and civilizations'),
                                                ('Literature', 'Study of written works and literary analysis');

-- 10 Courses
INSERT INTO courses (name, code, description, credits, department_id) VALUES
                                                                          ('Introduction to Java', 'CS101', 'Fundamentals of Java programming', 3, 1),
                                                                          ('Data Structures', 'CS201', 'Arrays, lists, trees, and graphs', 4, 1),
                                                                          ('Calculus I', 'MATH101', 'Limits, derivatives, and integrals', 4, 2),
                                                                          ('Linear Algebra', 'MATH201', 'Vectors, matrices, and transformations', 3, 2),
                                                                          ('Classical Mechanics', 'PHYS101', 'Newtonian mechanics and motion', 4, 3),
                                                                          ('Organic Chemistry', 'CHEM201', 'Carbon-based chemical compounds', 4, 4),
                                                                          ('Cell Biology', 'BIO101', 'Structure and function of cells', 3, 5),
                                                                          ('Thermodynamics', 'ENG201', 'Heat, work, and energy systems', 3, 6),
                                                                          ('Microeconomics', 'ECON101', 'Individual economic decision making', 3, 7),
                                                                          ('Ancient Philosophy', 'PHIL101', 'Greek and Roman philosophical thought', 3, 8);

-- 10 Enrollments
INSERT INTO enrollments (student_name, student_email, enrollment_date, course_id) VALUES
                                                                                      ('Alice Johnson', 'alice.johnson@university.edu', '2025-01-15', 1),
                                                                                      ('Bob Smith', 'bob.smith@university.edu', '2025-01-15', 1),
                                                                                      ('Charlie Brown', 'charlie.brown@university.edu', '2025-01-16', 2),
                                                                                      ('Diana Prince', 'diana.prince@university.edu', '2025-01-16', 3),
                                                                                      ('Edward Norton', 'edward.norton@university.edu', '2025-01-17', 3),
                                                                                      ('Fiona Apple', 'fiona.apple@university.edu', '2025-01-17', 5),
                                                                                      ('George Lucas', 'george.lucas@university.edu', '2025-01-18', 6),
                                                                                      ('Hannah Montana', 'hannah.montana@university.edu', '2025-01-18', 7),
                                                                                      ('Ivan Drago', 'ivan.drago@university.edu', '2025-01-19', 9),
                                                                                      ('Julia Roberts', 'julia.roberts@university.edu', '2025-01-19', 10);