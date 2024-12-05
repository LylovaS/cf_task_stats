package ru.nsu.fit.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.backend.cf_records.ContestApiResult;
import ru.nsu.fit.backend.cf_requests.CfRequester;


@RestController
public class SummaryStatisticsController {

    @GetMapping("/api_v1/sum_stats")
    public ContestApiResult person_stats(
            @RequestParam(value = "contestId") int contestId
    ) throws Exception {
        ContestApiResult result;
        result = CfRequester.requestContestResult(contestId);

        return result;
    }


    @ControllerAdvice
    public static class ExceptionHandlerAdvice {
        @ExceptionHandler(Exception.class)
        public ResponseEntity<Object> handleException(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
