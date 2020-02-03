package top.wsabc.gamification.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.wsabc.gamification.client.dto.MultiplicationResultAttempt;

@FeignClient("${multiplication.service-id}")
public interface MultiplicationClient {

    @GetMapping("/results/{id}")
    MultiplicationResultAttempt retrieveMultiplicationResultAttemptById(@PathVariable("id") Long attemptId);

}
