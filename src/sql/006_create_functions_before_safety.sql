CREATE OR REPLACE FUNCTION fn_persons_before_safety_trigger() RETURNS TRIGGER AS
$$
BEGIN
    IF NEW.is_deleted = TRUE AND OLD.is_deleted = FALSE THEN
        IF EXISTS (SELECT 1 FROM person_phones WHERE person_id = NEW.id) THEN
            RAISE EXCEPTION 'Нельзя удалить пользователя (is_deleted=true), пока у него есть привязанные телефоны!';
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION fn_phones_before_safety_trigger() RETURNS TRIGGER AS
$$
BEGIN
    IF EXISTS (SELECT 1 FROM persons WHERE id = NEW.person_id AND is_deleted = TRUE) THEN
        RAISE EXCEPTION 'Нельзя привязать или изменить телефон для пользователя, который помечен как удаленный!';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;