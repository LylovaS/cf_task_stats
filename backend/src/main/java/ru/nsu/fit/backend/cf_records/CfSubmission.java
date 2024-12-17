package ru.nsu.fit.backend.cf_records;

public record CfSubmission(
        long id,
        long contestId,
        long creationTimeSeconds,
        long relativeTimeSeconds,
        Problem problem,
        Author author,
        String programmingLanguage,
        String verdict,
        String testset,
        int passedTestCount,
        int timeConsumedMillis,
        long memoryConsumedBytes
) {
}
