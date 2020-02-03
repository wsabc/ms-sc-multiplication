package top.wsabc.multiplication.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MultiplicationRepository extends CrudRepository<Multiplication, Long> {

    Optional<Multiplication> findByFactorAAndFactorB(int factorA, int factorB);

}
