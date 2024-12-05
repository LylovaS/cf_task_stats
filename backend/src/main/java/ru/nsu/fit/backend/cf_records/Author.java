package ru.nsu.fit.backend.cf_records;

public record Author(long contestId, Member[] members, String participantType, boolean ghost) {
    public record Member(String handle) {
    }
}
