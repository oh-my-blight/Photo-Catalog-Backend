CREATE TRIGGER trg_persons_before_safety
    BEFORE UPDATE
    ON persons
    FOR EACH ROW
EXECUTE FUNCTION fn_persons_before_safety_trigger();

CREATE TRIGGER trg_phones_before_safety
    BEFORE INSERT OR UPDATE
    ON person_phones
    FOR EACH ROW
EXECUTE FUNCTION fn_phones_before_safety_trigger();

CREATE TRIGGER trg_persons_after_audit
    AFTER INSERT OR UPDATE OR DELETE
    ON persons
    FOR EACH ROW
EXECUTE FUNCTION fn_persons_after_audit_trigger();

CREATE TRIGGER trg_phones_after_audit
    AFTER INSERT OR UPDATE OR DELETE
    ON person_phones
    FOR EACH ROW
EXECUTE FUNCTION fn_phones_after_audit_trigger();