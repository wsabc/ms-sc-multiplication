package top.wsabc.gamification.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.wsabc.gamification.domain.LeaderBoardRow;
import top.wsabc.gamification.service.LeaderBoardService;

import java.util.List;

@RestController
@RequestMapping("/leaders")
public class LeaderBoardController {

    private LeaderBoardService leaderBoardService;

    @Autowired
    public LeaderBoardController(final LeaderBoardService leaderBoardService) {
        this.leaderBoardService = leaderBoardService;
    }

    @GetMapping
    public List<LeaderBoardRow> getLeaderBoard() {
        return leaderBoardService.getCurrentLeaderBoard();
    }

}
