package com.shriyan.tennis_ladder;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String showHomePage(Model model) {
        List<String> players = List.of(
            "Alex",
            "Maya",
            "Shriyan",
            "Jordan"
        );

        model.addAttribute("players", players);

        return "home";
    }
}
