ALTER TABLE wait_time_observation
    ALTER COLUMN performance_code DROP NOT NULL;

INSERT INTO schema_migration (version, description)
VALUES ('V003', 'Allow missing source performance codes')
ON CONFLICT (version) DO NOTHING;
