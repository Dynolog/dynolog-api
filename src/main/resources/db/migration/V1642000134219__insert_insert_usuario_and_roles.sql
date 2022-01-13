-- insert admin
INSERT INTO "user"
    ("name", email, password)
VALUES
    ('admin', 'admin@email.com', '$2a$10$QBuMJLbVmHzgvTwpxDynSetACNdCBjU5zgo.81RWEDzH46aUrgcNK');

-- insert roles
INSERT INTO "role"
    ("name", initials)
VALUES
    ('ADMINISTRADOR', 'ADM'),
    ('USER',          'USER');

-- put roles into admin
INSERT INTO user_role
    (user_id, role_id)
VALUES
    (
        (SELECT id FROM "user" WHERE email = 'admin@email.com'),
        (SELECT id FROM "role" WHERE initials = 'ADM')
    ),(
        (SELECT id FROM "user" WHERE email = 'admin@email.com'),
        (SELECT id FROM "role" WHERE initials = 'USER')
    );