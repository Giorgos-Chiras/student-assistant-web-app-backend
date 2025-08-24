CREATE TABLE users
(
    id         INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username   VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(100) NOT NULL,
    age        INT          NOT NULL,
    email      VARCHAR(100) NOT NULL UNIQUE,
    university VARCHAR(100) NOT NULL,
    major      VARCHAR(100) NOT NULL
);

CREATE TABLE professor
(
    id            INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    email         VARCHAR(100),
    office_number VARCHAR(100),
    user_id INT NOT NULL

);

CREATE TABLE course
(
    id           INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    course_id    VARCHAR(50) NOT NULL,
    name         VARCHAR(50) NOT NULL,
    ects         INT,
    professor_id INT NULL,
    CONSTRAINT fk_professor FOREIGN KEY (professor_id)
        REFERENCES professor (id)
        ON DELETE SET NULL,

    user_id      INT NULL,
    CONSTRAINT fk_course_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE

);

CREATE TABLE task
(
    id          INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    due_date    DATE,
    due_time    TIME,
    user_id     INT NULL,
    course_id   INT NULL,

     CONSTRAINT fk_task_user
        FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE,

    CONSTRAINT fk_course
        FOREIGN KEY (course_id)
            REFERENCES course (id)
            ON DELETE SET NULL,
    grade VARCHAR(20),
    completed BIT NOT NULL
);


