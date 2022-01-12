--liquibase formatted sql
--changeset eduardo.comerlato:1
CREATE TABLE IF NOT EXISTS associate
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY,
    cpf         VARCHAR (11),
    name        VARCHAR (80),
    PRIMARY KEY (id)
);

--changeset eduardo.comerlato:2
CREATE TABLE IF NOT EXISTS schedule
(
    id                          BIGINT GENERATED ALWAYS AS IDENTITY,
    duration_in_seconds         INTEGER,
    subject                     VARCHAR (100),
    closed                      BOOLEAN,
    PRIMARY KEY (id)
);

--changeset eduardo.comerlato:3
CREATE TABLE IF NOT EXISTS vote
(
    id                          BIGINT GENERATED ALWAYS AS IDENTITY,
    schedule_id                 BIGINT,
    associate_id                BIGINT,
    answer                      VARCHAR (5),
    FOREIGN KEY (schedule_id) REFERENCES schedule (id),
    PRIMARY KEY (id)
);

--changeset eduardo.comerlato:4
CREATE TABLE IF NOT EXISTS schedule_results
(
    schedule_id                 BIGINT PRIMARY KEY,
    voted_yes                   BIGINT,
    voted_no                    BIGINT,
    result                      VARCHAR (5),
    FOREIGN KEY (schedule_id) REFERENCES schedule (id)
);