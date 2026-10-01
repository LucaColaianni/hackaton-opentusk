CREATE TABLE IF NOT EXISTS schema_migration (
    version VARCHAR(20) PRIMARY KEY,
    description TEXT NOT NULL,
    applied_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS open_data_source (
    source_key VARCHAR(100) PRIMARY KEY,
    title TEXT NOT NULL,
    dataset_url TEXT NOT NULL,
    distribution_url TEXT NOT NULL,
    owner TEXT NOT NULL,
    license TEXT NOT NULL,
    temporal_coverage TEXT NOT NULL,
    data_updated_at DATE,
    metadata_verified_at DATE NOT NULL,
    file_sha256 CHAR(64) NOT NULL,
    imported_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS wait_time_observation (
    source_key VARCHAR(100) NOT NULL REFERENCES open_data_source(source_key),
    asl_code VARCHAR(6) NOT NULL,
    asl_name TEXT NOT NULL,
    reference_year INTEGER NOT NULL,
    reference_period TEXT NOT NULL,
    performance_id INTEGER NOT NULL,
    performance_description TEXT NOT NULL,
    performance_code TEXT NOT NULL,
    reservations INTEGER,
    reservations_to_guarantee INTEGER,
    guarantee_b INTEGER,
    guarantee_b_tmax INTEGER,
    guarantee_d INTEGER,
    guarantee_d_tmax INTEGER,
    guarantee_p INTEGER,
    guarantee_p_tmax INTEGER,
    PRIMARY KEY (source_key, asl_code, reference_year, reference_period, performance_id)
);

CREATE INDEX IF NOT EXISTS idx_wait_time_asl_period
    ON wait_time_observation (asl_code, reference_year, reference_period);

INSERT INTO schema_migration (version, description)
VALUES ('V001', 'Create open data source and wait time observations')
ON CONFLICT (version) DO NOTHING;
