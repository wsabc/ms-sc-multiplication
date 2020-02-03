package top.wsabc.gamification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.wsabc.gamification.domain.LeaderBoardRow;
import top.wsabc.gamification.domain.ScoreCardRepository;

import java.util.List;

@Service
public class LeaderBoardService {

    private final ScoreCardRepository scoreCardRepository;

    @Autowired
    public LeaderBoardService(final ScoreCardRepository scoreCardRepository) {
        this.scoreCardRepository = scoreCardRepository;
    }

    public List<LeaderBoardRow> getCurrentLeaderBoard() {
        return scoreCardRepository.findFirst10();
    }
}
