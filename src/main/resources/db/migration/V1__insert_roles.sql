-- V1__insert_roles.sql
-- V1__create_roles_table.sql
CREATE TABLE IF NOT EXISTS roles (
     id SERIAL PRIMARY KEY,
     name VARCHAR(50) UNIQUE NOT NULL
);

INSERT INTO roles (name) VALUES ('ROLE_USER')
    ON CONFLICT (id) DO NOTHING;

INSERT INTO roles (name) VALUES ( 'ROLE_MODERATOR')
    ON CONFLICT (id) DO NOTHING;

INSERT INTO roles (name) VALUES ( 'ROLE_ADMIN')
    ON CONFLICT (id) DO NOTHING;