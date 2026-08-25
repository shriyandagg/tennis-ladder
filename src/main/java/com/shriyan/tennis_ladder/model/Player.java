package com.shriyan.tennis_ladder.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int ladderPosition;

    public Player() {
    }

    public Player(String name, int ladderPosition) {
        this.name = name;
        this.ladderPosition = ladderPosition;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLadderPosition() {
        return ladderPosition;
    }
}