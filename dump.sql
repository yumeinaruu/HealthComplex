INSERT INTO users (id, name, created, changed)
VALUES (1, 'John Doe', NOW(), NOW());


INSERT INTO security (id, login, password, role, user_id)
VALUES (1, 'admin', '$2a$12$UHla3meM7DlDPUVZOBavU.XW9xjCR8DhxW1wzZoYG1Bu.U0XikInG', 'ADMIN', 1);

SELECT * FROM users;

SELECT * FROM security;