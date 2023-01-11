CREATE TABLE tasks (
   id SERIAL PRIMARY KEY,
   description TEXT,
   created TIMESTAMP,
   done BOOLEAN,
   CONSTRAINT task_limits UNIQUE (description,created)
);
