INSERT INTO users (username, password, age, email, university, major)
VALUES
    ('alice123', '$2b$12$LFco1B1iK87XLkTajriDVezgqHuqNJAwY3Ah6va8rCG0BPi6wIGm6', 20, 'alice@example.com', 'Harvard University', 'Computer Science'),
    ('bob456', '$2b$12$R/fw5SKpgQ4cqS8Vm/Ldn.0k.ibqg545DVGmVgSg6U9RWXJuRpGua', 22, 'bob@example.com', 'MIT', 'Electrical Engineering'),
    ('charlie789', '$2b$12$p5DCYN1g4kERJHd7zhg3mu/9o.g5IitscT.277xmhXcEgdBInncZa', 21, 'charlie@example.com', 'Stanford University', 'Physics'),
    ('dana001', '$2b$12$yqr/Bbo.DJiANrTzF9aYYuka6dZ19Fdi8BRYXN6Ey2OQNLZNo3PO2', 23, 'dana@example.com', 'UC Berkeley', 'Mathematics'),
    ('eric007', '$2b$12$lI9TwyfDuIwifjxP5Omlz.rkcDrM2qKr.j6xmXVg3tl1Bt8usSWJS', 19, 'eric@example.com', 'Oxford University', 'Philosophy');


INSERT INTO professor (name, email, office_number,user_id)
VALUES ('John Smith', 'john.smith@university.edu', 'B-101',1),
       ('Lisa Williams', 'lisa.williams@university.edu', 'C-202',2),
       ('Michael Jordan', 'michael.jordan@university.edu', 'D-303',3);

INSERT INTO course (course_id, name, ects, professor_id, user_id)
VALUES ('CS101', 'Intro to Programming', 6, 1, 1),
       ('EE202', 'Circuit Analysis', 5, 2, 2),
       ('PH301', 'Quantum Mechanics', 7, 3, 3),
       ('MATH210', 'Linear Algebra', 5, 2, 4),
       ('PHIL100', 'Intro to Philosophy', 4, NULL, 5);

INSERT INTO task (name, description, due_date, due_time, user_id, course_id,grade,completed)
VALUES
    ('Project Proposal', 'Submit project proposal document', '2025-09-15', '17:00:00', 1, 1,'75/100',true),
    ('Midterm Exam', 'Complete midterm exam covering chapters 1-5', '2025-10-10', '09:30:00', 2, 2,'',false),
    ('Final Presentation', 'Prepare slides and present project', '2025-12-05', '14:00:00', 3, 3,'',false);