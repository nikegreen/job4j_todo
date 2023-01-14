CREATE TABLE IF NOT EXISTS todo_user (
    id SERIAL PRIMARY KEY,
    name text NOT NULL,
    login text NOT NULL unique,
    password text NOT NULL
);
comment on table todo_user is 'Список всех пользователей';
comment on column todo_user.id is 'идентификатор пользоватнля';
comment on column todo_user.name is 'имя пользователя';
comment on column todo_user.login is 'логин. должен быть уникальным в таблице';
comment on column todo_user.password is 'пароль';