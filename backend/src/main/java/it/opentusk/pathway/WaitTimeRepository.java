package it.opentusk.pathway;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

@ApplicationScoped
public class WaitTimeRepository {

    private static final String SOURCE_KEY = "puglia-wait-times-2024-10-07-11";

    @Inject
    DataSource dataSource;

    public PathwayResponse.WaitTimeContext loadAslBariContext() {
        try (var connection = dataSource.getConnection()) {
            var source = loadSource(connection);
            var records = loadRecords(connection);
            var sourceDetails = source.orElseGet(() -> new SourceDetails(
                    "Monitoraggio tempi di attesa ex ante — 7–11 ottobre 2024",
                    "Regione Puglia",
                    "https://dati.puglia.it/v2/dataset/monitoraggio-tempi-di-attesa",
                    "https://dati.puglia.it/ckan/dataset/8d6b91a6-9575-4dba-b4f0-f8771ce08825/resource/26096f59-111e-41ec-a726-281d1dd2dbdf/download/monitoraggio-tempi-di-attesa-07_11-ottobre-2024.csv",
                    "CC BY 4.0",
                    "7–11 ottobre 2024 (copertura del dataset: 13 luglio 2020 – 11 ottobre 2024)",
                    "2025-01-03",
                    "2026-10-01",
                    null));

            return new PathwayResponse.WaitTimeContext(
                    !records.isEmpty(),
                    sourceDetails.title(),
                    sourceDetails.owner(),
                    sourceDetails.datasetUrl(),
                    sourceDetails.distributionUrl(),
                    sourceDetails.license(),
                    sourceDetails.period(),
                    sourceDetails.dataUpdatedAt(),
                    sourceDetails.metadataVerifiedAt(),
                    sourceDetails.importedAt(),
                    records.size(),
                    "Dati aggregati storici per ASL Bari. Non indicano disponibilità attuali o attese individuali. "
                            + "Il catalogo non descrive tutte le colonne numeriche: i valori sono mostrati senza "
                            + "interpretazioni o suggerimenti.",
                    records);
        } catch (SQLException exception) {
            throw new IllegalStateException("Impossibile leggere il contesto open data dal database", exception);
        }
    }

    private Optional<SourceDetails> loadSource(java.sql.Connection connection) throws SQLException {
        var query = """
                SELECT title, owner, dataset_url, distribution_url, license, temporal_coverage,
                       data_updated_at, metadata_verified_at, imported_at
                FROM open_data_source
                WHERE source_key = ?
                """;
        try (var statement = connection.prepareStatement(query)) {
            statement.setString(1, SOURCE_KEY);
            try (var result = statement.executeQuery()) {
                if (!result.next()) {
                    return Optional.empty();
                }
                return Optional.of(new SourceDetails(
                        result.getString("title"),
                        result.getString("owner"),
                        result.getString("dataset_url"),
                        result.getString("distribution_url"),
                        result.getString("license"),
                        result.getString("temporal_coverage"),
                        result.getDate("data_updated_at") == null ? null : result.getDate("data_updated_at").toString(),
                        result.getDate("metadata_verified_at").toString(),
                        result.getTimestamp("imported_at").toInstant().toString()));
            }
        }
    }

    private List<PathwayResponse.WaitTimeRecord> loadRecords(java.sql.Connection connection) throws SQLException {
        var records = new ArrayList<PathwayResponse.WaitTimeRecord>();
        var query = """
                SELECT performance_id, performance_description, performance_code, reservations,
                       reservations_to_guarantee, guarantee_b, guarantee_b_tmax, guarantee_d,
                       guarantee_d_tmax, guarantee_p, guarantee_p_tmax
                FROM wait_time_observation
                WHERE source_key = ? AND asl_code = '160114'
                ORDER BY CASE WHEN performance_id ~ '^[0-9]+'
                              THEN substring(performance_id FROM '^[0-9]+')::INTEGER
                              ELSE 2147483647 END,
                         performance_id
                """;
        try (var statement = connection.prepareStatement(query)) {
            statement.setString(1, SOURCE_KEY);
            try (var result = statement.executeQuery()) {
                while (result.next()) {
                    records.add(new PathwayResponse.WaitTimeRecord(
                            result.getString("performance_id"),
                            result.getString("performance_description"),
                            result.getString("performance_code"),
                            nullableInt(result, "reservations"),
                            nullableInt(result, "reservations_to_guarantee"),
                            nullableInt(result, "guarantee_b"),
                            nullableInt(result, "guarantee_b_tmax"),
                            nullableInt(result, "guarantee_d"),
                            nullableInt(result, "guarantee_d_tmax"),
                            nullableInt(result, "guarantee_p"),
                            nullableInt(result, "guarantee_p_tmax")));
                }
            }
        }
        return records;
    }

    private Integer nullableInt(ResultSet result, String column) throws SQLException {
        var value = result.getObject(column);
        return value == null ? null : ((Number) value).intValue();
    }

    private record SourceDetails(String title, String owner, String datasetUrl, String distributionUrl,
                                 String license, String period, String dataUpdatedAt,
                                 String metadataVerifiedAt, String importedAt) {}
}
