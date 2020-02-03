package top.wsabc.multiplication;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import top.wsabc.multiplication.domain.Multiplication;
import top.wsabc.multiplication.domain.MultiplicationRepository;
import top.wsabc.multiplication.domain.MultiplicationResultAttempt;
import top.wsabc.multiplication.domain.MultiplicationResultAttemptRepository;
import top.wsabc.multiplication.domain.User;
import top.wsabc.multiplication.domain.UserRepository;
import top.wsabc.multiplication.event.EventDispatcher;
import top.wsabc.multiplication.service.MultiplicationService;
import top.wsabc.multiplication.service.RandomGeneratorService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class MultiplicationServiceTest {

    @Mock
    private RandomGeneratorService randomGeneratorService;

    private MultiplicationService multiplicationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MultiplicationResultAttemptRepository attemptRepository;

    @Mock
    MultiplicationRepository multiplicationRepository;

    @Mock
    EventDispatcher eventDispatcher;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        multiplicationService = new MultiplicationService(randomGeneratorService, userRepository,
                multiplicationRepository, attemptRepository, eventDispatcher);
    }

    @Test
    public void createRandomMultiplicationTest() {
        given(randomGeneratorService.generateRandomFactor()).willReturn(55, 20);

        Multiplication multiplication = multiplicationService.createRandomMultiplication();

        assertThat(multiplication.getFactorA()).isEqualTo(55);
        assertThat(multiplication.getFactorB()).isEqualTo(20);
        multiplication.setResult(1100);
        assertThat(multiplication.getResult()).isEqualTo(1100);
    }

    @Test
    public void retrieveStatsTest() {
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("john_doe");
        MultiplicationResultAttempt attempt1 = new MultiplicationResultAttempt(user, multiplication, 3010, false);
        MultiplicationResultAttempt attempt2 = new MultiplicationResultAttempt(user, multiplication, 3030, false);
        List<MultiplicationResultAttempt> latestAttempts = Lists.newArrayList(attempt1, attempt2);
        given(userRepository.findByAlias("john_doe")).willReturn(Optional.empty());
        given(attemptRepository.findTop5ByUserAliasOrderByIdDesc("john_doe")).willReturn(latestAttempts);

        List<MultiplicationResultAttempt> johnDoeStats = multiplicationService.getStatsForUser("john_doe");
        assertThat(johnDoeStats).isEqualTo(latestAttempts);
    }

}
