package com.shriyan.tennis_ladder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shriyan.tennis_ladder.model.TournamentMatch;

public interface TournamentMatchRepository
        extends JpaRepository<TournamentMatch, Long> {

    List<TournamentMatch> findAllByOrderByPlayedAtDesc();
}