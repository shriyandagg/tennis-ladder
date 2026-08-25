package com.shriyan.tennis_ladder.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.shriyan.tennis_ladder.model.Player;
import com.shriyan.tennis_ladder.repository.PlayerRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PlayerRepository playerRepository;

    public DataInitializer(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public void run(String... args) {
        if (playerRepository.count() == 0) {
            playerRepository.save(new Player("Alex", 1));
            playerRepository.save(new Player("Maya", 2));
            playerRepository.save(new Player("Shriyan", 3));
            playerRepository.save(new Player("Jordan", 4));
        }
    }
}
