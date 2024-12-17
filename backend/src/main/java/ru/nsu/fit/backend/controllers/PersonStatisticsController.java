package ru.nsu.fit.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.backend.TaskSubmissions;
import ru.nsu.fit.backend.cf_records.ContestApiResult;
import ru.nsu.fit.backend.cf_records.CfSubmission;
import ru.nsu.fit.backend.cf_requests.CfRequester;
import ru.nsu.fit.backend.stat_records.PersonStatRecord;

import static ru.nsu.fit.backend.utils.HandleChecker.check_handle;

@RestController
public class PersonStatisticsController {

    @GetMapping("/api_v1/person_stats")
    public PersonStatRecord person_stats(
            @RequestParam(value = "handle") String handle,
            @RequestParam(value = "contestId") int contestId,
            @RequestParam(value = "taskName") String name

    ) throws Exception {
        if (!check_handle(handle)) {
            throw new Exception("handle must match regular expression [a-zA-Z0-9_-]+]");
        }
        TaskSubmissions allSubmissions = new TaskSubmissions(name, contestId);
        ContestApiResult personSubmissions = CfRequester.requestPersonResult(contestId, handle);
        int n = 0;
        String taskIndex = "no submissions", taskName = "no submissions";
        for (CfSubmission submission: personSubmissions.result()) {
            if (!submission.verdict().equals("OK")) continue;
            if (!(submission.problem().name().equals(name) || submission.problem().index().equals(name))) continue;
            ++n;
            taskName = submission.problem().name();
            taskIndex = submission.problem().index();
        }

        PersonStatRecord.SubmissionStatRecord[] statRecords = new PersonStatRecord.SubmissionStatRecord[n];
        n = 0;
        for (CfSubmission submission: personSubmissions.result()) {
            if (!submission.verdict().equals("OK")) continue;
            if (!(submission.problem().name().equals(name) || submission.problem().index().equals(name))) continue;
            statRecords[n] = allSubmissions.calcSubmissionStatRecord(submission);
            ++n;
        }

        return new PersonStatRecord(handle, contestId, taskIndex, taskName, statRecords);
    }


    @ControllerAdvice
    public static class ExceptionHandlerAdvice {
        @ExceptionHandler(Exception.class)
        public ResponseEntity<Object> handleException(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
