package com.shriyan.tennis_ladder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shriyan.tennis_ladder.model.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findAllByOrderByLadderPositionAsc();
}