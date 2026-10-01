DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = current_schema()
          AND table_name = 'wait_time_observation'
          AND column_name = 'performance_id'
          AND data_type <> 'text'
    ) THEN
        ALTER TABLE wait_time_observation
            ALTER COLUMN performance_id TYPE TEXT USING performance_id::TEXT;
    END IF;
END $$;

INSERT INTO schema_migration (version, description)
VALUES ('V002', 'Preserve source performance identifiers as text')
ON CONFLICT (version) DO NOTHING;
