package ru.nsu.fit.backend.cf_records;

public record Submission(
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
