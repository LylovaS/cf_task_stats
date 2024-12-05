package ru.nsu.fit.backend.cf_records;

public record Problem(long contestId, String index, String name, String type, String[] tags) {
}
