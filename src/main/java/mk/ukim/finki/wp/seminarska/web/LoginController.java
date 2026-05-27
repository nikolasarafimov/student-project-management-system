package mk.ukim.finki.wp.seminarska.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/")
    public String home() {
        return "redirect:/projects";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}