\set ON_ERROR_STOP on
BEGIN;

CREATE TEMP TABLE staging_wait_times (
    asl TEXT,
    anno TEXT,
    settimana_indice TEXT,
    id_prestazione TEXT,
    desc_prestazione TEXT,
    cod_prestazione TEXT,
    prenotazioni TEXT,
    prenotazioni_dagarantire TEXT,
    prenotazioni_dagarantire_b TEXT,
    prenotazioni_dagarantire_b_tmax TEXT,
    prenotazioni_dagarantire_d TEXT,
    prenotazioni_dagarantire_d_tmax TEXT,
    prenotazioni_dagarantire_p TEXT,
    prenotazioni_dagarantire_p_tmax TEXT
);

COPY staging_wait_times FROM '/tmp/mosalute_monitoraggio.csv'
WITH (FORMAT CSV, HEADER TRUE, ENCODING 'WIN1252');

INSERT INTO open_data_source (
    source_key, title, dataset_url, distribution_url, owner, license,
    temporal_coverage, data_updated_at, metadata_verified_at, file_sha256, imported_at
)
VALUES (
    'puglia-wait-times-2024-10-07-11',
    'Monitoraggio tempi di attesa ex ante — 7–11 ottobre 2024',
    'https://dati.puglia.it/v2/dataset/monitoraggio-tempi-di-attesa',
    'https://dati.puglia.it/ckan/dataset/8d6b91a6-9575-4dba-b4f0-f8771ce08825/resource/26096f59-111e-41ec-a726-281d1dd2dbdf/download/monitoraggio-tempi-di-attesa-07_11-ottobre-2024.csv',
    'Regione Puglia',
    'CC BY 4.0',
    '13 luglio 2020 – 11 ottobre 2024; file importato: 7–11 ottobre 2024',
    DATE '2025-01-03',
    DATE '2026-10-01',
    '03115c0c5396f62d728334337e10c986151109c921cda69132ced45f60807f2e',
    CURRENT_TIMESTAMP
)
ON CONFLICT (source_key) DO UPDATE SET
    title = EXCLUDED.title,
    dataset_url = EXCLUDED.dataset_url,
    distribution_url = EXCLUDED.distribution_url,
    owner = EXCLUDED.owner,
    license = EXCLUDED.license,
    temporal_coverage = EXCLUDED.temporal_coverage,
    data_updated_at = EXCLUDED.data_updated_at,
    metadata_verified_at = EXCLUDED.metadata_verified_at,
    file_sha256 = EXCLUDED.file_sha256,
    imported_at = CURRENT_TIMESTAMP;

INSERT INTO wait_time_observation (
    source_key, asl_code, asl_name, reference_year, reference_period,
    performance_id, performance_description, performance_code, reservations,
    reservations_to_guarantee, guarantee_b, guarantee_b_tmax,
    guarantee_d, guarantee_d_tmax, guarantee_p, guarantee_p_tmax
)
SELECT
    'puglia-wait-times-2024-10-07-11',
    asl,
    'ASL Bari',
    NULLIF(anno, '')::INTEGER,
    settimana_indice,
    COALESCE(id_prestazione, ''),
    desc_prestazione,
    cod_prestazione,
    NULLIF(prenotazioni, '')::INTEGER,
    NULLIF(prenotazioni_dagarantire, '')::INTEGER,
    NULLIF(prenotazioni_dagarantire_b, '')::INTEGER,
    NULLIF(prenotazioni_dagarantire_b_tmax, '')::INTEGER,
    NULLIF(prenotazioni_dagarantire_d, '')::INTEGER,
    NULLIF(prenotazioni_dagarantire_d_tmax, '')::INTEGER,
    NULLIF(prenotazioni_dagarantire_p, '')::INTEGER,
    NULLIF(prenotazioni_dagarantire_p_tmax, '')::INTEGER
FROM staging_wait_times
WHERE asl = '160114'
ON CONFLICT (source_key, asl_code, reference_year, reference_period, performance_id)
DO UPDATE SET
    asl_name = EXCLUDED.asl_name,
    performance_description = EXCLUDED.performance_description,
    performance_code = EXCLUDED.performance_code,
    reservations = EXCLUDED.reservations,
    reservations_to_guarantee = EXCLUDED.reservations_to_guarantee,
    guarantee_b = EXCLUDED.guarantee_b,
    guarantee_b_tmax = EXCLUDED.guarantee_b_tmax,
    guarantee_d = EXCLUDED.guarantee_d,
    guarantee_d_tmax = EXCLUDED.guarantee_d_tmax,
    guarantee_p = EXCLUDED.guarantee_p,
    guarantee_p_tmax = EXCLUDED.guarantee_p_tmax;

COMMIT;
