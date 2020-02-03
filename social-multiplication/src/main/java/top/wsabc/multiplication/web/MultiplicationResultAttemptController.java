package top.wsabc.multiplication.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.wsabc.multiplication.domain.MultiplicationResultAttempt;
import top.wsabc.multiplication.service.MultiplicationService;

import java.util.List;

@RestController
@RequestMapping("/results")
@Slf4j
public class MultiplicationResultAttemptController {

    private final MultiplicationService multiplicationService;

    @Value("${server.port}")
    private int serverPort;

    @Autowired
    public MultiplicationResultAttemptController(MultiplicationService multiplicationService) {
        this.multiplicationService = multiplicationService;
    }

    @PostMapping
    public boolean postResult(@RequestBody MultiplicationResultAttempt attempt) {
        return multiplicationService.checkAttempt(attempt);
    }

    @GetMapping
    public List<MultiplicationResultAttempt> getStats(@RequestParam("alias") String alias) {
        return multiplicationService.getStatsForUser(alias);
    }

    @GetMapping("/{id}")
    public MultiplicationResultAttempt getAttemptById(@PathVariable("id") Long attemptId) {
        log.info("Retrieving result {} from server @ {}", attemptId, serverPort);
        return multiplicationService.getAttemptById(attemptId);
    }

}
