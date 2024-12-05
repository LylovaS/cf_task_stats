package ru.nsu.fit.backend.cf_requests;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.nsu.fit.backend.cf_records.BadRequestResult;
import ru.nsu.fit.backend.cf_records.ContestApiResult;

import java.util.Objects;

public class CfRequester {
    public static final String PERSON_API_TEMPLATE = "https://codeforces.com/api/contest.status?contestId=%d&handle=%s";
    public static final String CONTEST_API_TEMPLATE = "https://codeforces.com/api/contest.status?contestId=%d";

    static private ContestApiResult requestApi(final String url) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
        ResponseEntity<ContestApiResult> response;

        response = restTemplate.getForEntity(url, ContestApiResult.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            ResponseEntity<BadRequestResult> badResponse = restTemplate.getForEntity(url, BadRequestResult.class);
            if (response.getBody() == null) {
                throw new Exception("no body in request");
            }
            throw new Exception(Objects.requireNonNull(badResponse.getBody()).comment());
        }
        if (response.getBody() == null) {
            throw new Exception("no body in request");
        }

        return response.getBody();
    }

    static public ContestApiResult requestPersonResult(final int contestId, final String handle) throws Exception {
        String url = String.format(PERSON_API_TEMPLATE, contestId, handle);
        return requestApi(url);
    }

    static public ContestApiResult requestContestResult(final int contestId) throws Exception {
        String url = String.format(CONTEST_API_TEMPLATE, contestId);
        return requestApi(url);
    }
}
