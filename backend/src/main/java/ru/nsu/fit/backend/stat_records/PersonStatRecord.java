package ru.nsu.fit.backend.stat_records;

public record PersonStatRecord(
        String handle,
        long contestId,
        String taskIndex,
        String taskName,
        SubmissionStatRecord[] submissions
) {
    public record SubmissionStatRecord(
            long id,
            int timeConsumedMillis,
            long memoryConsumedBytes,
            int numberFasterSolutions,
            double percentageFasterSolutions,
            int numberSmallerSolutions,
            double percentageSmallerSolutions
    ) {
    }
}
