INSERT INTO users (id, name, created, changed)
VALUES (1, 'admin', NOW(), NOW());

INSERT INTO users (id, name, created, changed)
VALUES (2, 'coach', NOW(), NOW());

INSERT INTO users (id, name, created, changed)
VALUES (3, 'cashier', NOW(), NOW());

INSERT INTO users (id, name, created, changed)
VALUES (4, 'user', NOW(), NOW());


INSERT INTO security (id, login, password, role, user_id)
VALUES (1, 'admin', '$2a$12$UHla3meM7DlDPUVZOBavU.XW9xjCR8DhxW1wzZoYG1Bu.U0XikInG', 'ADMIN', 1);

INSERT INTO security (id, login, password, role, user_id)
VALUES (2, 'coach', '$2a$12$UHla3meM7DlDPUVZOBavU.XW9xjCR8DhxW1wzZoYG1Bu.U0XikInG', 'COACH', 2);

INSERT INTO security (id, login, password, role, user_id)
VALUES (3, 'cashier', '$2a$12$UHla3meM7DlDPUVZOBavU.XW9xjCR8DhxW1wzZoYG1Bu.U0XikInG', 'CASHIER', 3);

INSERT INTO security (id, login, password, role, user_id)
VALUES (4, 'user', '$2a$12$UHla3meM7DlDPUVZOBavU.XW9xjCR8DhxW1wzZoYG1Bu.U0XikInG', 'USER', 4);

SELECT * FROM users;

SELECT * FROM security;