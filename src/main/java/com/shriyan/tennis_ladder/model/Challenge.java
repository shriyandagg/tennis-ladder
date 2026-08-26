package com.shriyan.tennis_ladder.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "challenges")
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "challenger_id", nullable = false)
    private Player challenger;

    @ManyToOne
    @JoinColumn(name = "opponent_id", nullable = false)
    private Player opponent;

    @Enumerated(EnumType.STRING)
    private ChallengeStatus status;

    private LocalDateTime createdAt;

    public Challenge() {
    }

    public Challenge(Player challenger, Player opponent) {
        this.challenger = challenger;
        this.opponent = opponent;
        this.status = ChallengeStatus.PENDING_COACH_APPROVAL;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Player getChallenger() {
        return challenger;
    }

    public Player getOpponent() {
        return opponent;
    }

    public ChallengeStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setStatus(ChallengeStatus status) {
        this.status = status;
    }
}
