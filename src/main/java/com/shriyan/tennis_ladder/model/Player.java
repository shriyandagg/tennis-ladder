package com.shriyan.tennis_ladder.model;

public class Player {

    private String name;
    private int ladderPosition;

    public Player(String name, int ladderPosition) {
        this.name = name;
        this.ladderPosition = ladderPosition;
    }

    public String getName() {
        return name;
    }

    public int getLadderPosition() {
        return ladderPosition;
    }
}
