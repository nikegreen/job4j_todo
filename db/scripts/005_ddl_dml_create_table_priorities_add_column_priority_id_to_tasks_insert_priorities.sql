CREATE TABLE priorities (
   id SERIAL PRIMARY KEY,
   name TEXT UNIQUE NOT NULL,
   position int
);
comment on table tasks is 'Список всех приоритетов задач';
comment on column priorities.id is 'id приоритета для таблицы priorities, ключевое поле';
comment on column priorities.name is 'обозначение приоритета, уникальная строка для поля';
comment on column priorities.position is 'уровень приоритета чем больше значение, тем ниже приоритет';


INSERT INTO priorities (name, position) VALUES ('urgently', 1);
INSERT INTO priorities (name, position) VALUES ('normal', 2);

ALTER TABLE tasks ADD COLUMN priority_id int REFERENCES priorities(id);
comment on column tasks.priority_id is 'id приоритета назначенный для задачи из таблицы priorities';

UPDATE tasks SET priority_id = (SELECT id FROM priorities WHERE name = 'urgently');
