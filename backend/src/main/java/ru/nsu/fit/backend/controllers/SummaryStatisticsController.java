package ru.nsu.fit.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.backend.TaskSubmissions;
import ru.nsu.fit.backend.stat_records.TaskStatRecord;


@RestController
public class SummaryStatisticsController {

    @GetMapping("/api_v1/sum_stats")
    public TaskStatRecord person_stats(
            @RequestParam(value = "contestId") int contestId,
            @RequestParam(value = "taskName") String taskName
    ) throws Exception {
        TaskSubmissions submissions = new TaskSubmissions(taskName, contestId);
        return submissions.getStatRecord();
    }


    @ControllerAdvice
    public static class ExceptionHandlerAdvice {
        @ExceptionHandler(Exception.class)
        public ResponseEntity<Object> handleException(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
