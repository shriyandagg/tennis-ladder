package com.shriyan.tennis_ladder;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shriyan.tennis_ladder.model.Player;

@Controller
public class HomeController {

    @GetMapping("/")
    public String showHomePage(Model model) {
        List<Player> players = List.of(
            new Player("Alex", 1),
            new Player("Maya", 2),
            new Player("Shriyan", 3),
            new Player("Jordan", 4)
        );

        model.addAttribute("players", players);

        return "home";
    }
}
