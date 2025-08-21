package mk.ukim.finki.wp.seminarska.web;

import mk.ukim.finki.wp.seminarska.model.AppUser;
import mk.ukim.finki.wp.seminarska.model.Role;
import mk.ukim.finki.wp.seminarska.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        AppUser newUser = new AppUser();
        newUser.setRole(null); // ensure no default role
        model.addAttribute("user", newUser);
        model.addAttribute("roles", Role.values());
        return "register";
    }


    @PostMapping
    public String register(@ModelAttribute AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/login";
    }
}