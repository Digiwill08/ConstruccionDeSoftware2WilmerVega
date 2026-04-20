MERGE INTO users (username, password, role, status) KEY(username)
VALUES ('pancracio', '123456', 'INTERNAL_ANALYST', 'ACTIVE');

MERGE INTO users (username, password, role, status) KEY(username)
VALUES ('admin', 'admin123', 'ADMINISTRATOR', 'ACTIVE');