package com.shriyan.tennis_ladder.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shriyan.tennis_ladder.model.Challenge;
import com.shriyan.tennis_ladder.model.ChallengeStatus;
import com.shriyan.tennis_ladder.model.Player;
import com.shriyan.tennis_ladder.repository.ChallengeRepository;
import com.shriyan.tennis_ladder.repository.PlayerRepository;

@Service
public class LadderService {

    private final ChallengeRepository challengeRepository;
    private final PlayerRepository playerRepository;

    public LadderService(
            ChallengeRepository challengeRepository,
            PlayerRepository playerRepository) {
        this.challengeRepository = challengeRepository;
        this.playerRepository = playerRepository;
    }

    @Transactional
    public void recordResult(
            Long challengeId,
            Long winnerId,
            String score) {

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Challenge not found"));

        if (challenge.getStatus() != ChallengeStatus.APPROVED) {
            throw new IllegalStateException(
                    "Only approved challenges can receive results"
            );
        }

        Player winner = playerRepository.findById(winnerId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Winner not found"));

        boolean winnerParticipated =
                winner.getId().equals(challenge.getChallenger().getId())
                || winner.getId().equals(challenge.getOpponent().getId());

        if (!winnerParticipated) {
            throw new IllegalArgumentException(
                    "Winner must be one of the challenge participants"
            );
        }

        if (score == null || score.isBlank()) {
            throw new IllegalArgumentException("Score is required");
        }

        if (winner.getId().equals(challenge.getChallenger().getId())) {
            moveChallengerUp(challenge.getChallenger(),
                    challenge.getOpponent());
        }

        challenge.complete(winner, score.trim());
        challengeRepository.save(challenge);
    }

    private void moveChallengerUp(
            Player challenger,
            Player opponent) {

        int oldPosition = challenger.getLadderPosition();
        int newPosition = opponent.getLadderPosition();

        List<Player> players =
                playerRepository.findAllByOrderByLadderPositionAsc();

        for (Player player : players) {
            int position = player.getLadderPosition();

            if (position >= newPosition && position < oldPosition) {
                player.setLadderPosition(position + 1);
            }
        }

        challenger.setLadderPosition(newPosition);
        playerRepository.saveAll(players);
    }
}
