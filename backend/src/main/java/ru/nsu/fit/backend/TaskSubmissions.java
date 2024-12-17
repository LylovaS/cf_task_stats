package ru.nsu.fit.backend;

import ru.nsu.fit.backend.cf_records.CfSubmission;
import ru.nsu.fit.backend.cf_records.ContestApiResult;
import ru.nsu.fit.backend.cf_requests.CfRequester;
import ru.nsu.fit.backend.stat_records.PersonStatRecord;
import ru.nsu.fit.backend.stat_records.TaskStatRecord;

import java.util.Arrays;
import java.util.Comparator;

public class TaskSubmissions {
    private String taskName;
    private String taskIndex;
    private final long contestId;
    private final Submission[] submissionsSortedByTime;
    private final Submission[] submissionsSortedByMemory;

    private record Submission(long id, int timeConsumedMillis, long memoryConsumedBytes) {
    }

    public TaskSubmissions(String name, long contestId) throws Exception {
        ContestApiResult result = CfRequester.requestContestResult(contestId);
        this.contestId = contestId;
        int n = 0;
        for (var submission : result.result()) {
            if (!submission.verdict().equals("OK")) continue;
            if (!(submission.problem().name().equals(name) || submission.problem().index().equals(name))) continue;
            ++n;
            this.taskName = submission.problem().name();
            this.taskIndex = submission.problem().index();
        }
        this.submissionsSortedByMemory = new Submission[n];
        this.submissionsSortedByTime = new Submission[n];
        n = 0;
        for (var submission : result.result()) {
            if (!submission.verdict().equals("OK")) continue;
            if (!(submission.problem().name().equals(name) || submission.problem().index().equals(name))) continue;
            this.submissionsSortedByTime[n] = new Submission(submission.id(), submission.timeConsumedMillis(), submission.memoryConsumedBytes());
            this.submissionsSortedByMemory[n] = new Submission(submission.id(), submission.timeConsumedMillis(), submission.memoryConsumedBytes());
            ++n;
        }
        Arrays.sort(this.submissionsSortedByTime, Comparator.comparingInt(Submission::timeConsumedMillis));
        Arrays.sort(this.submissionsSortedByMemory, Comparator.comparingLong(Submission::memoryConsumedBytes));
    }


    static int BUCKETS_NUMBER = 20;

    public TaskStatRecord getStatRecord() {
        TaskStatRecord.HistBucket[] timeBuckets = new TaskStatRecord.HistBucket[BUCKETS_NUMBER];
        {
            int n = submissionsSortedByTime.length;
            int minTime = 0, maxTime = n == 0 ? 0 : submissionsSortedByTime[n - 1].timeConsumedMillis();
            int pastUsed = 0;
            for (int i = 0; i < BUCKETS_NUMBER; ++i) {
                int l = minTime + (i * (maxTime - minTime)) / BUCKETS_NUMBER;
                int r = minTime + ((i + 1) * (maxTime - minTime)) / BUCKETS_NUMBER - (i + 1 == BUCKETS_NUMBER ? 0 : 1);
                int numberInBucket = 0;
                while (pastUsed < n && submissionsSortedByTime[pastUsed].timeConsumedMillis() <= r) {
                    ++pastUsed;
                    ++numberInBucket;
                }
                timeBuckets[i] = new TaskStatRecord.HistBucket(l, r, numberInBucket, (double) numberInBucket / n * 100);
            }
        }
        TaskStatRecord.HistBucket[] memoryBuckets = new TaskStatRecord.HistBucket[BUCKETS_NUMBER];
        {
            int n = submissionsSortedByMemory.length;
            long minMemory = 0, maxMemory = n == 0 ? 0 : submissionsSortedByMemory[n - 1].memoryConsumedBytes();
            int pastUsed = 0;
            for (int i = 0; i < BUCKETS_NUMBER; ++i) {
                long l = minMemory + (i * (maxMemory - minMemory)) / BUCKETS_NUMBER;
                long r = minMemory + ((i + 1) * (maxMemory - minMemory)) / BUCKETS_NUMBER - (i + 1 == BUCKETS_NUMBER ? 0 : 1);
                int numberInBucket = 0;
                while (pastUsed < n && submissionsSortedByMemory[pastUsed].memoryConsumedBytes() <= r) {
                    ++pastUsed;
                    ++numberInBucket;
                }
                memoryBuckets[i] = new TaskStatRecord.HistBucket(l, r, numberInBucket, (double) numberInBucket / n * 100);
            }
        }
        return new TaskStatRecord(this.contestId, this.taskIndex, this.taskName, timeBuckets, memoryBuckets);
    }

    public PersonStatRecord.SubmissionStatRecord calcSubmissionStatRecord(CfSubmission submission) {
        int n = submissionsSortedByTime.length;
        int numberFasterSolutions;
        {
            int l = 0, r = n;
            while (l != r) {
                int m = (l + r) / 2;
                if (submissionsSortedByTime[m].timeConsumedMillis() >= submission.timeConsumedMillis()) {
                    r = m;
                } else {
                    l = m + 1;
                }
            }
            numberFasterSolutions = l;
        }
        int numberSmallerSolutions;
        {
            int l = 0, r = n;
            while (l != r) {
                int m = (l + r) / 2;
                if (submissionsSortedByMemory[m].memoryConsumedBytes() >= submission.memoryConsumedBytes()) {
                    r = m;
                } else {
                    l = m + 1;
                }
            }
            numberSmallerSolutions = l;
        }
        return new PersonStatRecord.SubmissionStatRecord(
                submission.id(),
                submission.timeConsumedMillis(),
                submission.memoryConsumedBytes(),
                numberFasterSolutions,
                (double)numberFasterSolutions / n * 100,
                numberSmallerSolutions,
                (double) numberSmallerSolutions / n * 100
                );
    }
}
