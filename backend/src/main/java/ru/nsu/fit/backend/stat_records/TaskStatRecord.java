package ru.nsu.fit.backend.stat_records;

public record TaskStatRecord(
        long contestId,
        String taskIndex,
        String taskName,
        HistBucket[] timeBuckets,
        HistBucket[] memoryBuckets
) {
    public record HistBucket(
        long leftBoundary,
        long rightBoundary,
        int numberSubmissions,
        double percentageNumberSubmissions
    ) {
    }
}
