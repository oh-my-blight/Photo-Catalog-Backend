INSERT INTO phone_types (type_code, name) VALUES
                                              ('mobile', 'Мобильный'),
                                              ('home', 'Домашний'),
                                              ('work', 'Рабочий');

INSERT INTO persons (first_name, patronymic, last_name) VALUES
                                                            ('Иван', 'Иванович', 'Иванов'),
                                                            ('Петр', 'Петрович', 'Петров'),
                                                            ('Анна', 'Сергеевна', 'Сидорова');

INSERT INTO person_phones (person_id, phone_type_id, country_code, area_code, number)
VALUES
    (
        (SELECT id FROM persons WHERE first_name = 'Иван' AND last_name = 'Иванов' LIMIT 1),
        (SELECT id FROM phone_types WHERE type_code = 'mobile' LIMIT 1),
        '+7', '900', '123-45-67'
    ),
    (
        (SELECT id FROM persons WHERE first_name = 'Петр' AND last_name = 'Петров' LIMIT 1),
        (SELECT id FROM phone_types WHERE type_code = 'home' LIMIT 1),
        '7', '495', '987-65-43'
    ),
    (
        (SELECT id FROM persons WHERE first_name = 'Анна' AND last_name = 'Сидорова' LIMIT 1),
        (SELECT id FROM phone_types WHERE type_code = 'work' LIMIT 1),
        '+7', '812', '555-00-11'
    );