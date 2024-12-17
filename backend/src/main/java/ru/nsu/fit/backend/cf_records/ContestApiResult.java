package ru.nsu.fit.backend.cf_records;

public record ContestApiResult(String status, CfSubmission[] result) {
}
