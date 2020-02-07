package top.wsabc.multiplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import top.wsabc.multiplication.domain.Multiplication;
import top.wsabc.multiplication.domain.MultiplicationRepository;
import top.wsabc.multiplication.domain.MultiplicationResultAttempt;
import top.wsabc.multiplication.domain.MultiplicationResultAttemptRepository;
import top.wsabc.multiplication.domain.User;
import top.wsabc.multiplication.domain.UserRepository;
import top.wsabc.multiplication.event.EventDispatcher;
import top.wsabc.multiplication.event.MultiplicationSolvedEvent;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

@Service
public class MultiplicationService {

    final RandomGeneratorService randomGeneratorService;

    MultiplicationResultAttemptRepository multiplicationResultAttemptRepository;

    UserRepository userRepository;

    MultiplicationRepository multiplicationRepository;

    EventDispatcher eventDispatcher;

    @Autowired
    public MultiplicationService(RandomGeneratorService randomGeneratorService,
                                 UserRepository userRepository,
                                 MultiplicationRepository multiplicationRepository,
                                 MultiplicationResultAttemptRepository attemptRepository,
                                 final EventDispatcher dispatcher) {
        this.randomGeneratorService = randomGeneratorService;
        this.userRepository = userRepository;
        this.multiplicationRepository = multiplicationRepository;
        this.multiplicationResultAttemptRepository = attemptRepository;
        this.eventDispatcher = dispatcher;
    }

    public Multiplication createRandomMultiplication() {
        return new Multiplication(randomGeneratorService.generateRandomFactor(), randomGeneratorService.generateRandomFactor());
    }

    @Transactional(rollbackOn = Exception.class)
    public boolean checkAttempt(MultiplicationResultAttempt attempt) {
        Optional<User> user = userRepository.findByAlias(attempt.getUser().getAlias());

        Multiplication multiplication = attempt.getMultiplication();
        multiplication.setResult(multiplication.getFactorA() * multiplication.getFactorB());

        Optional<Multiplication> dbMultiplication = multiplicationRepository.findByFactorAAndFactorB(multiplication.getFactorA(), multiplication.getFactorB());

        boolean correct = attempt.getResultAttempt() == multiplication.getResult();
        Assert.isTrue(!attempt.isCorrect(), "You can't send an attempt marked as correct!!");

        MultiplicationResultAttempt checkedAttempt = new MultiplicationResultAttempt(
                user.orElse(attempt.getUser()),
                dbMultiplication.orElse(attempt.getMultiplication()),
                attempt.getResultAttempt(),
                correct
        );
        multiplicationResultAttemptRepository.save(checkedAttempt);

        eventDispatcher.send(new MultiplicationSolvedEvent(checkedAttempt.getId(), checkedAttempt.getUser().getId(), checkedAttempt.isCorrect()));

        return correct;
    }

    public List<MultiplicationResultAttempt> getStatsForUser(String userAlias) {
        return multiplicationResultAttemptRepository.findTop5ByUserAliasOrderByIdDesc(userAlias);
    }

    public MultiplicationResultAttempt getAttemptById(Long attemptId) {
        return multiplicationResultAttemptRepository.findById(attemptId).orElse(null);
    }

}
