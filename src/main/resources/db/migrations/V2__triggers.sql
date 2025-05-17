-- Function to log actions
CREATE OR REPLACE FUNCTION log_action()
RETURNS TRIGGER AS $$
BEGIN
INSERT INTO audit_logs (id, entity_type, entity_id, action, performed_by, details)
VALUES (
           gen_random_uuid(),
           TG_TABLE_NAME,
           CASE
               WHEN TG_OP = 'DELETE' THEN OLD.id
               ELSE NEW.id
               END,
           TG_OP,
           current_user,
           jsonb_build_object(
                   'name', COALESCE(NEW.name, OLD.name),
                   'category', COALESCE(NEW.category, OLD.category)
           )
       );
RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Triggers for resources table
CREATE TRIGGER resource_insert_trigger
    AFTER INSERT ON resources
    FOR EACH ROW EXECUTE FUNCTION log_action();

CREATE TRIGGER resource_update_trigger
    AFTER UPDATE ON resources
    FOR EACH ROW EXECUTE FUNCTION log_action();

CREATE TRIGGER resource_delete_trigger
    AFTER DELETE ON resources
    FOR EACH ROW EXECUTE FUNCTION log_action();