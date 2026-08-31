package com.shriyan.tennis_ladder;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.shriyan.tennis_ladder.model.ChallengeStatus;
import com.shriyan.tennis_ladder.model.Player;
import com.shriyan.tennis_ladder.repository.ChallengeRepository;
import com.shriyan.tennis_ladder.repository.PlayerRepository;
import com.shriyan.tennis_ladder.repository.TournamentMatchRepository;
import org.springframework.security.core.Authentication;

@Controller
public class HomeController {

    private final PlayerRepository playerRepository;
    private final ChallengeRepository challengeRepository;
    private final TournamentMatchRepository tournamentMatchRepository;

    public HomeController(
            PlayerRepository playerRepository,
            ChallengeRepository challengeRepository,
            TournamentMatchRepository tournamentMatchRepository) {
        this.playerRepository = playerRepository;
        this.challengeRepository = challengeRepository;
        this.tournamentMatchRepository = tournamentMatchRepository;
    }

    @GetMapping("/")
    public String showHomePage(
        Model model,
        Authentication authentication) {
        model.addAttribute(
                "players",
                playerRepository.findAllByOrderByLadderPositionAsc()
        );

        model.addAttribute(
                "activeChallenges",
                challengeRepository
                        .findAllByStatusInOrderByCreatedAtDesc(
                                List.of(
                                        ChallengeStatus
                                                .PENDING_COACH_APPROVAL,
                                        ChallengeStatus.APPROVED
                                )
                        )
        );

        model.addAttribute(
                "matchHistory",
                challengeRepository
                        .findAllByStatusOrderByCompletedAtDesc(
                                ChallengeStatus.COMPLETED
                        )
        );

        model.addAttribute(
                "tournamentMatches",
                tournamentMatchRepository
                        .findAllByOrderByPlayedAtDesc()
        );

        boolean isCoach = authentication.getAuthorities()
        .stream()
        .anyMatch(authority ->
                authority.getAuthority().equals("ROLE_COACH"));

        model.addAttribute("isCoach", isCoach);

        return "home";
    }

    @GetMapping("/players/new")
    public String showPlayerForm(Model model) {
        model.addAttribute("player", new Player());

        return "new-player";
    }

    @PostMapping("/players")
    public String addPlayer(
            @ModelAttribute Player player) {

        int nextPosition =
                (int) playerRepository.count() + 1;

        player.setLadderPosition(nextPosition);
        playerRepository.save(player);

        return "redirect:/";
    }
}