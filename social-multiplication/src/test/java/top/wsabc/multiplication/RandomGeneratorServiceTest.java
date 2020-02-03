package top.wsabc.multiplication;

import org.junit.Before;
import org.junit.Test;
import top.wsabc.multiplication.service.RandomGeneratorService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class RandomGeneratorServiceTest {

    private RandomGeneratorService randomGeneratorService;

    @Before
    public void setUp() {
        randomGeneratorService = new RandomGeneratorService();
    }

    @Test
    public void generatedRandomFactorIsBetweenExpectedLimits() {
        List<Integer> randomFactors = IntStream.range(0, 1000)
                .map(i -> randomGeneratorService.generateRandomFactor())
                .boxed().collect(Collectors.toList());
        assertThat(randomFactors).containsOnlyElementsOf(
                IntStream.range(11, 100).boxed().collect(Collectors.toList()));
    }

}
