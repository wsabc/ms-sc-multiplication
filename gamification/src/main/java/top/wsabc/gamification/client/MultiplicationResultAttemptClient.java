package top.wsabc.gamification.client;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import top.wsabc.gamification.client.dto.MultiplicationResultAttempt;

@Component
public class MultiplicationResultAttemptClient {

    private final RestTemplate restTemplate;
    private final String multiplicationHost;

    @Autowired
    MultiplicationClient multiplicationFeignClient;

    @Autowired
    public MultiplicationResultAttemptClient(RestTemplate restTemplate, @Value("${multiplicationHost}") String multiplicationHost) {
        this.restTemplate = restTemplate;
        this.multiplicationHost = multiplicationHost;
    }

    @HystrixCommand(fallbackMethod = "defaultResult")
    public MultiplicationResultAttempt retrieveMultiplicationResultAttemptById(final Long attemptId) {
//        return restTemplate.getForObject(multiplicationHost + "/results/" + attemptId, MultiplicationResultAttempt.class);
        MultiplicationResultAttempt resultAttempt = multiplicationFeignClient.retrieveMultiplicationResultAttemptById(attemptId);
        System.out.println("====>" + resultAttempt);
        return resultAttempt;
    }

    private MultiplicationResultAttempt defaultResult(final Long attemptId) {
        // fallback method must have the same method signature.
        return new MultiplicationResultAttempt("fakeAlias", 10, 10, 100, true);
    }

}
