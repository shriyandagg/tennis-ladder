package com.shriyan.tennis_ladder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shriyan.tennis_ladder.repository.PlayerRepository;
import com.shriyan.tennis_ladder.service.LadderService;

@Controller
public class TournamentController {

    private final PlayerRepository playerRepository;
    private final LadderService ladderService;

    public TournamentController(
            PlayerRepository playerRepository,
            LadderService ladderService) {
        this.playerRepository = playerRepository;
        this.ladderService = ladderService;
    }

    @GetMapping("/coach/tournament-results/new")
    public String showTournamentResultForm(Model model) {
        model.addAttribute(
                "players",
                playerRepository.findAllByOrderByLadderPositionAsc()
        );

        return "tournament-result";
    }

    @PostMapping("/coach/tournament-results")
    public String recordTournamentResult(
            @RequestParam Long playerOneId,
            @RequestParam Long playerTwoId,
            @RequestParam Long winnerId,
            @RequestParam String score,
            @RequestParam String tournamentName,
            RedirectAttributes redirectAttributes) {

        try {
            ladderService.recordTournamentResult(
                    playerOneId,
                    playerTwoId,
                    winnerId,
                    score,
                    tournamentName
            );
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    exception.getMessage()
            );

            return "redirect:/coach/tournament-results/new";
        }

        return "redirect:/coach";
    }
}
