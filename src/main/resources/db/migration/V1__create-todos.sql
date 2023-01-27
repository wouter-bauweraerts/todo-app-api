CREATE TABLE todos
(
    todo_id     INTEGER      NOT NULL,
    description VARCHAR(255) NOT NULL,
    completed   BOOLEAN      NOT NULL,
    CONSTRAINT pk_todos PRIMARY KEY (todo_id)
);

ALTER TABLE todos
    ADD CONSTRAINT uc_todos_todo UNIQUE (todo_id);

CREATE INDEX idx_todos_completed ON todos(completed);