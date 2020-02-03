package top.wsabc.gamification.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreCardRepository extends CrudRepository<ScoreCard, Long> {

    @Query("select sum(s.score) from ScoreCard s where s.userId = :userId group by s.userId")
    int getTotalScoreForUser(@Param("userId") final Long userId);

    @Query("select new top.wsabc.gamification.domain.LeaderBoardRow(s.userId, sum(s.score)) from ScoreCard s group by s.userId order by sum(s.score) desc")
    List<LeaderBoardRow> findFirst10();

    List<ScoreCard> findByUserIdOrderByScoreTimestampDesc(final Long userId);

}
