CREATE INDEX IF NOT EXISTS idx_phones_person_id ON person_phones (person_id);
CREATE INDEX IF NOT EXISTS idx_phones_full_number ON person_phones (number);
CREATE INDEX IF NOT EXISTS idx_audit_log_new_data ON audit_log USING gin (new_data);
CREATE INDEX IF NOT EXISTS idx_pers_hist_id ON persons_history (person_id);
CREATE INDEX IF NOT EXISTS idx_phon_hist_id ON person_phones_history (phone_id);