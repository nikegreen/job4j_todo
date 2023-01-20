ALTER TABLE  todo_user ADD COLUMN user_zone text default '';
comment on column  todo_user.user_zone is 'название временной зоны для пользователя из списка TimeZone.getAvailableIDs()';
UPDATE todo_user SET user_zone = '' WHERE user_zone = null;