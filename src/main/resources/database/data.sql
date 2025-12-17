-- Заполнение таблицы тестовыми данными
INSERT INTO users (name, email, age, salary, created_at) VALUES
 ('Иван Иванов', 'ivan.ivanov@company.com', 28, 75000.00, '2025-05-15 09:30:00'),
 ('Анна Петрова', 'anna.petrova@company.com', 32, 82000.00, '2023-07-10 10:15:00'),
 ('Петр Сидоров', 'petr.sidorov@company.com', 25, 65000.00, '2021-11-17 11:45:00'),
 ('Мария Кузнецова', 'maria.kuznetsova@company.com', 35, 95000.00, '2024-01-21 14:20:00'),
 ('Алексей Смирнов', 'alexey.smirnov@company.com', 40, 105000.00, '2024-07-19 08:45:00'),
 ('Ольга Новикова', 'olga.novikova@company.com', 29, 72000.00, '2020-08-28 13:30:00'),
 ('Дмитрий Козлов', 'dmitry.kozlov@company.com', 31, 88000.00, '2019-03-21 16:10:00'),
 ('Екатерина Морозова', 'ekaterina.morozova@company.com', 27, 68000.00, '2025-04-22 09:00:00'),
 ('Сергей Волков', 'sergey.volkov@company.com', 33, 92000.00, '2023-09-23 11:20:00'),
 ('Наталья Зайцева', 'natalya.zaytseva@company.com', 26, 61000.00, '2024-10-24 15:45:00')
    ON CONFLICT (email) DO NOTHING;