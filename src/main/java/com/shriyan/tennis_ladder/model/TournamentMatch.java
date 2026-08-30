package com.shriyan.tennis_ladder.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tournament_matches")
public class TournamentMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_one_id", nullable = false)
    private Player playerOne;

    @ManyToOne
    @JoinColumn(name = "player_two_id", nullable = false)
    private Player playerTwo;

    @ManyToOne
    @JoinColumn(name = "winner_id", nullable = false)
    private Player winner;

    private String score;
    private String tournamentName;
    private LocalDateTime playedAt;

    public TournamentMatch() {
    }

    public TournamentMatch(
            Player playerOne,
            Player playerTwo,
            Player winner,
            String score,
            String tournamentName) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.winner = winner;
        this.score = score;
        this.tournamentName = tournamentName;
        this.playedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public Player getWinner() {
        return winner;
    }

    public String getScore() {
        return score;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public LocalDateTime getPlayedAt() {
        return playedAt;
    }
}
