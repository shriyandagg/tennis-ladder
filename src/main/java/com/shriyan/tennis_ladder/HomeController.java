package com.shriyan.tennis_ladder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shriyan.tennis_ladder.repository.PlayerRepository;

@Controller
public class HomeController {

    private final PlayerRepository playerRepository;

    public HomeController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @GetMapping("/")
    public String showHomePage(Model model) {
        model.addAttribute(
            "players",
            playerRepository.findAllByOrderByLadderPositionAsc()
        );

        return "home";
    }
}
