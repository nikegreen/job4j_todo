alter table tasks add column user_id int not null references todo_user (id);
comment on column tasks.user_id is 'id пользователя создавшего задачу из таблицы todo_user';

