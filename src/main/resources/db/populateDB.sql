DELETE FROM user_role;
DELETE FROM meals;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (dateTime, description, calories, user_id)
VALUES ('2023-06-20 10:00:00', 'Завтрак', 500, 100000),
       ('2023-06-20 13:00:00', 'Обед', 1000, 100000),
       ('2023-06-20 20:00:00', 'Ужин', 500, 100000),
       ('2023-06-21 0:00:00', 'Еда на граничное значение', 100, 100000),
       ('2023-06-21 10:00:00', 'Завтрак', 500, 100000),
       ('2023-06-21 13:00:00', 'Обед', 1000, 100000),
       ('2023-06-21 20:00:00', 'Ужин', 510, 100000),
       ('2023-06-21 14:00:00', 'Админ ланч', 510, 100001),
       ('2023-06-21 21:00:00', 'Админ ужин', 1500, 100001);