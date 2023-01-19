CREATE TABLE categories (
   id SERIAL PRIMARY KEY,
   name TEXT UNIQUE NOT NULL
);
comment on table categories is 'Список всех категорий задач';
comment on column categories.id is 'id категории для таблицы categories, ключевое поле';
comment on column categories.name is 'название категории, уникальная строка для поля';
