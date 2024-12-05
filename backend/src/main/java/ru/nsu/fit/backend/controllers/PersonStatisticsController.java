package ru.nsu.fit.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.backend.cf_records.ContestApiResult;
import ru.nsu.fit.backend.cf_requests.CfRequester;

import static ru.nsu.fit.backend.utils.HandleChecker.check_handle;

@RestController
public class PersonStatisticsController {

    @GetMapping("/api_v1/person_stats")
    public ContestApiResult person_stats(
            @RequestParam(value = "handle") String handle,
            @RequestParam(value = "contestId") int contestId
    ) throws Exception {
        if (!check_handle(handle)) {
            throw new Exception("handle must match regular expression [a-zA-Z0-9_-]+]");
        }
        ContestApiResult result;
        result = CfRequester.requestPersonResult(contestId, handle);

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
