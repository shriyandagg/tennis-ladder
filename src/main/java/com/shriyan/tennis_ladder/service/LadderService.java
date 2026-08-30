package com.shriyan.tennis_ladder.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shriyan.tennis_ladder.model.Challenge;
import com.shriyan.tennis_ladder.model.ChallengeStatus;
import com.shriyan.tennis_ladder.model.Player;
import com.shriyan.tennis_ladder.model.TournamentMatch;
import com.shriyan.tennis_ladder.repository.ChallengeRepository;
import com.shriyan.tennis_ladder.repository.PlayerRepository;
import com.shriyan.tennis_ladder.repository.TournamentMatchRepository;

@Service
public class LadderService {

    private final ChallengeRepository challengeRepository;
    private final PlayerRepository playerRepository;
    private final TournamentMatchRepository tournamentMatchRepository;

    public LadderService(
            ChallengeRepository challengeRepository,
            PlayerRepository playerRepository,
            TournamentMatchRepository tournamentMatchRepository) {
        this.challengeRepository = challengeRepository;
        this.playerRepository = playerRepository;
        this.tournamentMatchRepository = tournamentMatchRepository;
    }

    @Transactional
    public void recordResult(
            Long challengeId,
            Long winnerId,
            String score) {

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Challenge not found"
                        ));

        if (challenge.getStatus() != ChallengeStatus.APPROVED) {
            throw new IllegalStateException(
                    "Only approved challenges can receive results"
            );
        }

        Player winner = findPlayer(winnerId);

        boolean winnerParticipated =
                winner.getId().equals(
                        challenge.getChallenger().getId()
                )
                || winner.getId().equals(
                        challenge.getOpponent().getId()
                );

        if (!winnerParticipated) {
            throw new IllegalArgumentException(
                    "Winner must be one of the challenge participants"
            );
        }

        validateScore(score);

        if (winner.getId().equals(
                challenge.getChallenger().getId())) {
            moveLowerRankedWinnerUp(
                    challenge.getChallenger(),
                    challenge.getOpponent()
            );
        }

        challenge.complete(winner, score.trim());
        challengeRepository.save(challenge);
    }

    @Transactional
    public void recordTournamentResult(
            Long playerOneId,
            Long playerTwoId,
            Long winnerId,
            String score,
            String tournamentName) {

        if (playerOneId.equals(playerTwoId)) {
            throw new IllegalArgumentException(
                    "A player cannot play against themselves"
            );
        }

        Player playerOne = findPlayer(playerOneId);
        Player playerTwo = findPlayer(playerTwoId);
        Player winner = findPlayer(winnerId);

        boolean winnerParticipated =
                winner.getId().equals(playerOne.getId())
                || winner.getId().equals(playerTwo.getId());

        if (!winnerParticipated) {
            throw new IllegalArgumentException(
                    "Winner must be one of the selected players"
            );
        }

        validateScore(score);

        Player loser = winner.getId().equals(playerOne.getId())
                ? playerTwo
                : playerOne;

        if (winner.getLadderPosition()
                > loser.getLadderPosition()) {
            moveLowerRankedWinnerUp(winner, loser);
        }

        String eventName =
                tournamentName == null || tournamentName.isBlank()
                        ? "External Tournament"
                        : tournamentName.trim();

        TournamentMatch tournamentMatch =
                new TournamentMatch(
                        playerOne,
                        playerTwo,
                        winner,
                        score.trim(),
                        eventName
                );

        tournamentMatchRepository.save(tournamentMatch);
    }

    private Player findPlayer(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Player not found"
                        ));
    }

    private void validateScore(String score) {
        if (score == null || score.isBlank()) {
            throw new IllegalArgumentException(
                    "Score is required"
            );
        }
    }

    private void moveLowerRankedWinnerUp(
            Player winner,
            Player higherRankedPlayer) {

        int oldPosition = winner.getLadderPosition();
        int newPosition = higherRankedPlayer.getLadderPosition();

        List<Player> players =
                playerRepository.findAllByOrderByLadderPositionAsc();

        for (Player player : players) {
            int position = player.getLadderPosition();

            if (position >= newPosition
                    && position < oldPosition) {
                player.setLadderPosition(position + 1);
            }
        }

        winner.setLadderPosition(newPosition);
        playerRepository.saveAll(players);
    }
}