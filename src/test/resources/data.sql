-- ============================================
-- Test Fixture Data (~10 records per table)
-- ============================================

-- 10 Departments
INSERT INTO departments (id, name, description) VALUES
                                                    (1, 'Computer Science', 'Study of computation and information processing'),
                                                    (2, 'Mathematics', 'Study of numbers, quantities, and shapes'),
                                                    (3, 'Physics', 'Study of matter, energy, and fundamental forces'),
                                                    (4, 'Chemistry', 'Study of substances and their interactions'),
                                                    (5, 'Biology', 'Study of living organisms'),
                                                    (6, 'Engineering', 'Application of science to design and build'),
                                                    (7, 'Economics', 'Study of production and distribution of goods'),
                                                    (8, 'Philosophy', 'Study of fundamental nature of knowledge'),
                                                    (9, 'History', 'Study of past events and civilizations'),
                                                    (10, 'Literature', 'Study of written works and literary analysis');

-- 10 Courses (spread across departments)
INSERT INTO courses (id, name, code, description, credits, department_id) VALUES
                                                                              (1, 'Introduction to Java', 'CS101', 'Fundamentals of Java programming', 3, 1),
                                                                              (2, 'Data Structures', 'CS201', 'Arrays, lists, trees, and graphs', 4, 1),
                                                                              (3, 'Calculus I', 'MATH101', 'Limits, derivatives, and integrals', 4, 2),
                                                                              (4, 'Linear Algebra', 'MATH201', 'Vectors, matrices, and transformations', 3, 2),
                                                                              (5, 'Classical Mechanics', 'PHYS101', 'Newtonian mechanics and motion', 4, 3),
                                                                              (6, 'Organic Chemistry', 'CHEM201', 'Carbon-based chemical compounds', 4, 4),
                                                                              (7, 'Cell Biology', 'BIO101', 'Structure and function of cells', 3, 5),
                                                                              (8, 'Thermodynamics', 'ENG201', 'Heat, work, and energy systems', 3, 6),
                                                                              (9, 'Microeconomics', 'ECON101', 'Individual economic decision making', 3, 7),
                                                                              (10, 'Ancient Philosophy', 'PHIL101', 'Greek and Roman philosophical thought', 3, 8);

-- 10 Enrollments (spread across courses)
INSERT INTO enrollments (id, student_name, student_email, enrollment_date, course_id) VALUES
                                                                                          (1, 'Alice Johnson', 'alice.johnson@university.edu', '2025-01-15', 1),
                                                                                          (2, 'Bob Smith', 'bob.smith@university.edu', '2025-01-15', 1),
                                                                                          (3, 'Charlie Brown', 'charlie.brown@university.edu', '2025-01-16', 2),
                                                                                          (4, 'Diana Prince', 'diana.prince@university.edu', '2025-01-16', 3),
                                                                                          (5, 'Edward Norton', 'edward.norton@university.edu', '2025-01-17', 3),
                                                                                          (6, 'Fiona Apple', 'fiona.apple@university.edu', '2025-01-17', 5),
                                                                                          (7, 'George Lucas', 'george.lucas@university.edu', '2025-01-18', 6),
                                                                                          (8, 'Hannah Montana', 'hannah.montana@university.edu', '2025-01-18', 7),
                                                                                          (9, 'Ivan Drago', 'ivan.drago@university.edu', '2025-01-19', 9),
                                                                                          (10, 'Julia Roberts', 'julia.roberts@university.edu', '2025-01-19', 10);