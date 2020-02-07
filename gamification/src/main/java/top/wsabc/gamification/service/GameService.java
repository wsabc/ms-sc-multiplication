package top.wsabc.gamification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.wsabc.gamification.client.MultiplicationResultAttemptClient;
import top.wsabc.gamification.client.dto.MultiplicationResultAttempt;
import top.wsabc.gamification.domain.Badge;
import top.wsabc.gamification.domain.BadgeCard;
import top.wsabc.gamification.domain.BadgeCardRepository;
import top.wsabc.gamification.domain.GameStats;
import top.wsabc.gamification.domain.ScoreCard;
import top.wsabc.gamification.domain.ScoreCardRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GameService {

    public static final int LUCKY_NUMBER = 42;

    private ScoreCardRepository scoreCardRepository;
    private BadgeCardRepository badgeCardRepository;
    private MultiplicationResultAttemptClient attemptClient;

    GameService(ScoreCardRepository scoreCardRepository,
                    BadgeCardRepository badgeCardRepository,
                    MultiplicationResultAttemptClient client) {
        this.scoreCardRepository = scoreCardRepository;
        this.badgeCardRepository = badgeCardRepository;
        this.attemptClient = client;
    }

    public GameStats newAttemptForUser(Long userId, Long attemptId, boolean correct) {
        if (correct) {
            ScoreCard sc = new ScoreCard(userId, attemptId);
            scoreCardRepository.save(sc);
            log.debug("User with id {} scored {} points for attempt id {}", userId, sc.getScore(), attemptId);

            List<BadgeCard> badgeCards = processForBadges(userId, attemptId);

            return new GameStats(userId, sc.getScore(), badgeCards.stream().map(BadgeCard::getBadge).collect(Collectors.toList()));
        }
        return GameStats.emptyStats(userId);
    }

    public GameStats retrieveStatsForUser(Long userId) {
        Long totalScoreForUser = scoreCardRepository.getTotalScoreForUser(userId);
        int score = totalScoreForUser == null ? 0 : totalScoreForUser.intValue();
        List<BadgeCard> badges = badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId);
        return new GameStats(userId, score, badges.stream().map(BadgeCard::getBadge).collect(Collectors.toList()));
    }

    List<BadgeCard> processForBadges(Long userId, Long attemptId) {
        Long totalScoreForUser = scoreCardRepository.getTotalScoreForUser(userId);
        int totalScore = totalScoreForUser == null ? 0 : totalScoreForUser.intValue();

        List<ScoreCard> scoreCards = scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId);
        List<BadgeCard> badgeCards = badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId);

        checkAndGiveBadgeBasedOnScore(badgeCards, Badge.BRONZE_MULTIPLICATOR, totalScore, 100, userId).ifPresent(badgeCards::add);
        checkAndGiveBadgeBasedOnScore(badgeCards, Badge.SILVER_MULTIPLICATOR, totalScore, 500, userId).ifPresent(badgeCards::add);
        checkAndGiveBadgeBasedOnScore(badgeCards, Badge.GOLD_MULTIPLICATOR, totalScore, 999, userId).ifPresent(badgeCards::add);

        if (scoreCards.size() == 1 && !containsBadge(badgeCards, Badge.FIRST_WON)) {
            BadgeCard firstWonBadge = giveBadgeToUser(Badge.FIRST_WON, userId);
            badgeCards.add(firstWonBadge);
        }

        MultiplicationResultAttempt resultAttempt = attemptClient.retrieveMultiplicationResultAttemptById(attemptId);
        if (!containsBadge(badgeCards, Badge.LUCKY_NUMBER)
                && hasLuckyNumber(resultAttempt)) {
            BadgeCard luckyBadge = giveBadgeToUser(Badge.LUCKY_NUMBER, userId);
            badgeCards.add(luckyBadge);
        }

        return badgeCards;
    }

    private Optional<BadgeCard> checkAndGiveBadgeBasedOnScore(List<BadgeCard> badgeCards, Badge badge,
                                                              int totalScore, int threshold, Long userId) {
        if (totalScore >= threshold && !containsBadge(badgeCards, badge)) {
            return Optional.of(giveBadgeToUser(badge, userId));
        }
        return Optional.empty();
    }

    private boolean containsBadge(List<BadgeCard> badgeCards, Badge badge) {
        return badgeCards.stream().anyMatch(badgeCard -> badgeCard.getBadge().equals(badge));
    }

    private BadgeCard giveBadgeToUser(Badge badge, Long userId) {
        BadgeCard bc = new BadgeCard(userId, badge);
        badgeCardRepository.save(bc);
        log.info("User {} won a new badge {}", userId, badge);
        return bc;
    }

    private boolean hasLuckyNumber(MultiplicationResultAttempt attempt) {
        return attempt.getMultiplicationFactorA() == LUCKY_NUMBER || attempt.getMultiplicationFactorB() == LUCKY_NUMBER;
    }
}
