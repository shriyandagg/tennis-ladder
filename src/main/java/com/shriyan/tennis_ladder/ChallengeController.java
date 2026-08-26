package com.shriyan.tennis_ladder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shriyan.tennis_ladder.model.Challenge;
import com.shriyan.tennis_ladder.model.ChallengeStatus;
import com.shriyan.tennis_ladder.model.Player;
import com.shriyan.tennis_ladder.repository.ChallengeRepository;
import com.shriyan.tennis_ladder.repository.PlayerRepository;

@Controller
public class ChallengeController {

    private final ChallengeRepository challengeRepository;
    private final PlayerRepository playerRepository;

    public ChallengeController(
            ChallengeRepository challengeRepository,
            PlayerRepository playerRepository) {
        this.challengeRepository = challengeRepository;
        this.playerRepository = playerRepository;
    }

    @GetMapping("/challenges/new")
    public String showChallengeForm(Model model) {
        model.addAttribute(
                "players",
                playerRepository.findAllByOrderByLadderPositionAsc()
        );

        return "new-challenge";
    }

    @PostMapping("/challenges")
    public String createChallenge(
            @RequestParam Long challengerId,
            @RequestParam Long opponentId,
            RedirectAttributes redirectAttributes) {

        Player challenger = playerRepository.findById(challengerId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Challenger not found"
                ));

        Player opponent = playerRepository.findById(opponentId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Opponent not found"
                ));

        if (challenger.getId().equals(opponent.getId())) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "A player cannot challenge themselves."
            );
            return "redirect:/challenges/new";
        }

        if (opponent.getLadderPosition() >= challenger.getLadderPosition()) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "You must challenge a player ranked above you."
            );
            return "redirect:/challenges/new";
        }

        int positionDifference =
                challenger.getLadderPosition()
                - opponent.getLadderPosition();

        if (positionDifference > 3) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "You can only challenge up to three positions above you."
            );
            return "redirect:/challenges/new";
        }

        challengeRepository.save(
                new Challenge(challenger, opponent)
        );

        return "redirect:/";
    }

    @PostMapping("/challenges/{id}/approve")
    public String approveChallenge(@PathVariable Long id) { 
    Challenge challenge = findChallenge(id);

    if (challenge.getStatus()
            == ChallengeStatus.PENDING_COACH_APPROVAL) {
        challenge.setStatus(ChallengeStatus.APPROVED);
        challengeRepository.save(challenge);
    }

    return "redirect:/coach";
}

    @PostMapping("/challenges/{id}/reject")
    public String rejectChallenge(@PathVariable Long id) {
        Challenge challenge = findChallenge(id);

        if (challenge.getStatus() == ChallengeStatus.PENDING_COACH_APPROVAL) {
            challenge.setStatus(ChallengeStatus.REJECTED);
            challengeRepository.save(challenge);
        }

        return "redirect:/coach";
    }

    @GetMapping("/coach")
    public String showCoachDashboard(Model model) {
    model.addAttribute(
            "challenges",
            challengeRepository.findAllByOrderByCreatedAtDesc()
    );

    return "coach";
    }

    private Challenge findChallenge(Long id) {
        return challengeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Challenge not found"
                ));
    }
}
