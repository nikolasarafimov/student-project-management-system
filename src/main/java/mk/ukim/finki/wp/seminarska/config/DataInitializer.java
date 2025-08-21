package mk.ukim.finki.wp.seminarska.config;

import jakarta.annotation.PostConstruct;
import mk.ukim.finki.wp.seminarska.model.*;
import mk.ukim.finki.wp.seminarska.repository.ProjectRepository;
import mk.ukim.finki.wp.seminarska.repository.TechnologyRepository;
import mk.ukim.finki.wp.seminarska.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DataInitializer {

    private final ProjectRepository projectRepository;
    private final TechnologyRepository technologyRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(ProjectRepository projectRepository, TechnologyRepository technologyRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.projectRepository = projectRepository;
        this.technologyRepository = technologyRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        List<AppUser> teachers = new ArrayList<>();

        if (userRepository.findByUsername("student").isEmpty()) {
            AppUser student = new AppUser();
            student.setUsername("student");
            student.setPassword(passwordEncoder.encode("student"));
            student.setRole(Role.STUDENT);
            userRepository.save(student);
        }

        if (userRepository.findByUsername("teacher").isEmpty()) {
            AppUser teacher = new AppUser();
            teacher.setUsername("teacher");
            teacher.setPassword(passwordEncoder.encode("teacher"));
            teacher.setRole(Role.TEACHER);
            userRepository.save(teacher);
            teachers.add(teacher);
        } else {
            teachers.add(userRepository.findByUsername("teacher").get());
        }

        String[] additionalTeachers = {
                "marija.stojanov",
                "ivan.petrov",
                "elena.trajkovska"
        };

        for (String t : additionalTeachers) {
            Optional<AppUser> existing = userRepository.findByUsername(t);
            if (existing.isEmpty()) {
                AppUser teacher = new AppUser();
                teacher.setUsername(t);
                teacher.setPassword(passwordEncoder.encode("teacher123"));
                teacher.setRole(Role.TEACHER);
                userRepository.save(teacher);
                teachers.add(teacher);
            } else {
                teachers.add(existing.get());
            }
        }

        Technology springBoot = new Technology("Spring Boot");
        Technology react = new Technology("React");
        Technology java = new Technology("Java");
        technologyRepository.saveAll(List.of(springBoot, react, java));

        AppUser teacher1 = teachers.get(0);
        AppUser teacher2 = teachers.get(1);
        AppUser teacher3 = teachers.get(2);
        AppUser teacher4 = teachers.get(3);

        List<Label> labelsP1 = List.of(new Label("web-app"), new Label("crud"), new Label("mvc"));
        List<Technology> techP1 = List.of(springBoot, java);
        Project p1 = new Project(
                "Campus Clubs Portal",
                "A portal for student clubs to register, publish events, and manage members.",
                new ArrayList<>(labelsP1),
                new ArrayList<>(techP1),
                "WP-101",
                teacher1,
                null,
                "https://github.com/example/campus-clubs",
                Status.DRAFT
        );
        projectRepository.save(p1);

        List<Label> labelsP2 = List.of(new Label("events"), new Label("calendar"));
        List<Technology> techP2 = List.of(springBoot, react);
        Project p2 = new Project(
                "Campus Events Hub",
                "Extension that adds an events calendar with subscription and reminders.",
                new ArrayList<>(labelsP2),
                new ArrayList<>(techP2),
                "WP-201",
                teacher2,
                p1,
                "https://github.com/example/campus-events",
                Status.SUBMITTED
        );
        projectRepository.save(p2);

        List<Label> labelsP3 = List.of(new Label("api"), new Label("integration"));
        List<Technology> techP3 = List.of(java);
        Project p3 = new Project(
                "Library API Gateway",
                "Unified API that aggregates library catalog, reservations, and fines.",
                new ArrayList<>(labelsP3),
                new ArrayList<>(techP3),
                "WP-305",
                teacher3,
                null,
                "https://github.com/example/library-gateway",
                Status.APPROVED
        );
        projectRepository.save(p3);

        List<Label> labelsP4 = List.of(new Label("prototype"), new Label("ux"));
        List<Technology> techP4 = List.of(react);
        Project p4 = new Project(
                "Meal Planner Prototype",
                "Early UX prototype for weekly meal plans and grocery lists.",
                new ArrayList<>(labelsP4),
                new ArrayList<>(techP4),
                "WP-150",
                teacher4,
                null,
                "https://github.com/example/meal-planner",
                Status.REJECTED
        );
        projectRepository.save(p4);
    }
}