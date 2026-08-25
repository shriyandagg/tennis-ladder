package com.shriyan.tennis_ladder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.shriyan.tennis_ladder.model.Player;
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

    @GetMapping("/players/new")
    public String showNewPlayerForm(Model model) {
        model.addAttribute("player", new Player());

        return "new-player";
    }

    @PostMapping("/players")
    public String addPlayer(@ModelAttribute Player player) {
        int newPosition = Math.toIntExact(playerRepository.count() + 1);

        player.setLadderPosition(newPosition);
        playerRepository.save(player);

        return "redirect:/";
    }
}
