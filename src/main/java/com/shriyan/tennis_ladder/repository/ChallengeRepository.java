package com.shriyan.tennis_ladder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shriyan.tennis_ladder.model.Challenge;
import com.shriyan.tennis_ladder.model.ChallengeStatus;
import com.shriyan.tennis_ladder.model.Player;

public interface ChallengeRepository
        extends JpaRepository<Challenge, Long> {

    List<Challenge> findAllByOrderByCreatedAtDesc();

    boolean existsByChallengerAndStatusIn(
            Player challenger,
            List<ChallengeStatus> statuses
    );

    boolean existsByOpponentAndStatusIn(
            Player opponent,
            List<ChallengeStatus> statuses
    );

    List<Challenge> findAllByStatusNotOrderByCreatedAtDesc(
        ChallengeStatus status
);

List<Challenge> findAllByStatusOrderByCompletedAtDesc(
        ChallengeStatus status
);

}

