package top.wsabc.multiplication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import top.wsabc.multiplication.domain.Multiplication;
import top.wsabc.multiplication.service.MultiplicationService;
import top.wsabc.multiplication.service.RandomGeneratorService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SocialMultiplicationApplicationTests {

	@Test
	public void contextLoads() {
	}

	@MockBean
	RandomGeneratorService randomGeneratorService;

	@Autowired
	MultiplicationService multiplicationService;

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
	public void generatedRandomFactorIsBetweenExpectedLimits() {
		List<Integer> randomFactors = IntStream.range(0, 1000)
				.map(i -> randomGeneratorService.generateRandomFactor())
				.boxed().collect(Collectors.toList());
		assertThat(randomFactors).containsOnlyElementsOf(
				IntStream.range(11, 100).boxed().collect(Collectors.toList()));
	}

}
