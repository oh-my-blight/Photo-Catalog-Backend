CREATE OR REPLACE FUNCTION fn_persons_after_audit_trigger()
    RETURNS TRIGGER AS
$$
BEGIN
    IF (TG_OP = 'INSERT') THEN
        INSERT INTO audit_log(table_name, operation, row_id, new_data)
        VALUES ('persons', 'INSERT', NEW.id, to_jsonb(NEW));

        INSERT INTO persons_history(person_id, first_name, patronymic, last_name, is_deleted)
        VALUES (NEW.id, NEW.first_name, NEW.patronymic, NEW.last_name, NEW.is_deleted);

    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO audit_log(table_name, operation, row_id, old_data, new_data)
        VALUES ('persons', 'UPDATE', NEW.id, to_jsonb(OLD), to_jsonb(NEW));

        IF (OLD.first_name, OLD.patronymic, OLD.last_name, OLD.is_deleted) IS DISTINCT FROM (NEW.first_name, NEW.patronymic, NEW.last_name, NEW.is_deleted) THEN
            INSERT INTO persons_history(person_id, first_name, patronymic, last_name, is_deleted)
            VALUES (NEW.id, NEW.first_name, NEW.patronymic, NEW.last_name, NEW.is_deleted);
        END IF;

    ELSIF (TG_OP = 'DELETE') THEN
        INSERT INTO audit_log(table_name, operation, row_id, old_data)
        VALUES ('persons', 'DELETE', OLD.id, to_jsonb(OLD));
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;