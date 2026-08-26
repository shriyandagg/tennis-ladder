package com.shriyan.tennis_ladder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shriyan.tennis_ladder.model.Challenge;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    List<Challenge> findAllByOrderByCreatedAtDesc();
}
