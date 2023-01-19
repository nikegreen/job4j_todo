CREATE TABLE tasks_categories (
   id SERIAL PRIMARY KEY,
   task_id int not null references tasks (id),
   category_id int not null references categories (id)
);
comment on table tasks_categories is 'Список всех категорий в задаче (связь многие к многим, tasks -- categories)';
comment on column tasks_categories.id is 'id для таблицы tasks_categories, ключевое поле';
comment on column tasks_categories.task_id is 'id задачи';
comment on column tasks_categories.category_id is 'id категории';