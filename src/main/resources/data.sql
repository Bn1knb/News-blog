INSERT INTO user (username, password, role, state, first_name, last_name) SELECT 'ADMIN', '$2a$10$pZEHgUHszBbx4/EwACglWuc2VnfYfRtL3SEwdIl90leJTgLyypwPO', 'ROLE_ADMIN', 'ACTIVE', 'ADMIN', 'ADMIN' WHERE NOT EXISTS (SELECT * FROM user WHERE username='ADMIN');