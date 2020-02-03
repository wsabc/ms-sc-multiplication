package top.wsabc.multiplication.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.wsabc.multiplication.domain.Multiplication;
import top.wsabc.multiplication.service.MultiplicationService;

@RestController
@RequestMapping("/multiplications")
@Slf4j
public class MultiplicationController {

    private final MultiplicationService multiplicationService;

    @Value("${server.port}")
    private int serverPort;

    @Autowired
    public MultiplicationController(MultiplicationService multiplicationService) {
        this.multiplicationService = multiplicationService;
    }

    @GetMapping("/random")
    public Multiplication getRandomMultiplication() {
        log.info("Generating a random multiplication from server @ {}", serverPort);
        return multiplicationService.createRandomMultiplication();
    }

}
