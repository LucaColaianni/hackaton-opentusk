package it.opentusk.pathway;

import java.util.List;

public record PathwayResponse(
        DoctorProfile doctor,
        List<PathwayStep> steps,
        List<PathwayOutcome> outcomes,
        CupReference cup,
        WaitTimeContext openDataContext) {

    public record DoctorProfile(String displayName, String role, String territory, boolean mock, String note) {}

    public record PathwayStep(String id, String title, String action, String responsible, List<String> documents,
                              String boundary) {}

    public record PathwayOutcome(String id, String label, String message, String nextStep, boolean leadsToCup) {}

    public record CupReference(String title, String url, String owner, String verifiedAt,
                               List<String> bringWithYou, String limitation) {}

    public record WaitTimeContext(boolean available, String title, String owner, String datasetUrl,
                                  String distributionUrl, String license, String period, String dataUpdatedAt,
                                  String metadataVerifiedAt, String importedAt, int recordCount,
                                  String limitation, List<WaitTimeRecord> records) {}

    public record WaitTimeRecord(String performanceId, String description, String code, Integer reservations,
                                 Integer reservationsToGuarantee, Integer guaranteeB, Integer guaranteeBTmax,
                                 Integer guaranteeD, Integer guaranteeDTmax, Integer guaranteeP,
                                 Integer guaranteePTmax) {}
}
