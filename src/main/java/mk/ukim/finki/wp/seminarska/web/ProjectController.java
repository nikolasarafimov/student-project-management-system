package mk.ukim.finki.wp.seminarska.web;

import jakarta.validation.Valid;
import mk.ukim.finki.wp.seminarska.model.*;
import mk.ukim.finki.wp.seminarska.repository.UserRepository;
import mk.ukim.finki.wp.seminarska.service.LabelService;
import mk.ukim.finki.wp.seminarska.service.ProjectService;
import mk.ukim.finki.wp.seminarska.service.TechnologyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final LabelService labelService;
    private final TechnologyService technologyService;
    private final UserRepository userRepository;

    public ProjectController(ProjectService projectService, LabelService labelService, TechnologyService technologyService, UserRepository userRepository) {
        this.projectService = projectService;
        this.labelService = labelService;
        this.technologyService = technologyService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String listProjects(
            @RequestParam(required = false) String searchText,
            Model model) {

        model.addAttribute("labels", labelService.findDistinctLabelsByName());
        model.addAttribute("technologies", technologyService.findAll());
        model.addAttribute("searchText", searchText);
        List<Project> projects;

        if (searchText != null && !searchText.isBlank()) {
            projects = projectService.findBySearchText(searchText);
        } else {
            projects = projectService.findAll();
        }

        model.addAttribute("projects", projects);
        return "projects";
    }

    @GetMapping("/add-form")
    @PreAuthorize("hasRole('STUDENT')")
    public String addProjectForm(Model model) {
        model.addAttribute("project", new Project());
        model.addAttribute("labels", labelService.findDistinctLabelsByName());
        model.addAttribute("technologies", technologyService.findAll());
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("statuses", Status.values());
        List<Project> projectsToExtendFrom=projectService.findAll();
        model.addAttribute("projectsToExtendFrom", projectsToExtendFrom);
        List<AppUser> teachers = userRepository.findByRole(Role.TEACHER);
        model.addAttribute("teachers", teachers);
        return "form";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('STUDENT')")
    public String saveProject(
            @Valid @ModelAttribute("project") Project project,
            BindingResult bindingResult,
            @RequestParam(required = false, defaultValue = "") String newLabels,
            @RequestParam(required = false, defaultValue = "") String newTechnologies,
            @RequestParam(required = false) Long extendedFromId,
            @RequestParam Long responsibleTeacherId,
            Model model) {

        AppUser responsibleTeacher = userRepository.findById(responsibleTeacherId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid teacher ID"));
        project.setResponsibleTeacher(responsibleTeacher);


        project.setStatus(Status.DRAFT);

        if (bindingResult.hasErrors()) {
            model.addAttribute("labels", labelService.findAll());
            model.addAttribute("technologies", technologyService.findAll());
            model.addAttribute("projects", projectService.findAll());
            model.addAttribute("statuses", Status.values());
            model.addAttribute("newLabels", newLabels);
            model.addAttribute("newTechnologies", newTechnologies);
            model.addAttribute("labelsStr", newLabels);
            model.addAttribute("technologiesStr", newTechnologies);
            return "form";
        }

        if (extendedFromId != null) {
            try {
                Project base = projectService.findById(extendedFromId);
                if (!base.getId().equals(project.getId())) {
                    project.setExtendedFrom(base);
                }
            } catch (IllegalArgumentException ex) {
                bindingResult.reject("extendedFrom.invalid", "Selected base project does not exist.");
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("labels", labelService.findAll());
            model.addAttribute("technologies", technologyService.findAll());
            model.addAttribute("projects", projectService.findAll());
            model.addAttribute("statuses", Status.values());
            model.addAttribute("newLabels", newLabels);
            model.addAttribute("newTechnologies", newTechnologies);
            return "form";
        }

        List<Label> labels = new ArrayList<>();
        for (String name : newLabels.split(",")) {
            String trimmed = name.trim();
            if (!trimmed.isEmpty()) {
                Label l = new Label();
                l.setName(trimmed);
                labelService.save(l);
                labels.add(l);
            }
        }
        project.setLabels(labels);

        List<Technology> technologies = new ArrayList<>();
        for (String name : newTechnologies.split(",")) {
            String trimmed = name.trim();
            if (!trimmed.isEmpty()) {
                Technology t = new Technology();
                t.setTechnologyName(trimmed);
                technologyService.save(t);
                technologies.add(t);
            }
        }
        project.setTechnologies(technologies);

        projectService.save(project);
        return "redirect:/projects";
    }

    @GetMapping("/edit-form/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public String editProjectForm(@PathVariable Long id, Model model) {
        Project project1 = projectService.findById(id);

        String labelsStr = project1.getLabels().stream()
                .map(Label::getName)
                .collect(Collectors.joining(", "));
        String technologiesStr = project1.getTechnologies().stream()
                .map(Technology::getTechnologyName)
                .collect(Collectors.joining(", "));

        List<Project> projectsToExtendFrom = new ArrayList<>(projectService.findAll());
        projectsToExtendFrom.remove(project1);
        List<AppUser> teachers = userRepository.findByRole(Role.TEACHER);
        model.addAttribute("teachers", teachers);
        model.addAttribute("projectsToExtendFrom", projectsToExtendFrom);
        model.addAttribute("labelsStr", labelsStr);
        model.addAttribute("technologiesStr", technologiesStr);
        model.addAttribute("project", project1);
        model.addAttribute("labels", labelService.findAll());
        model.addAttribute("technologies", technologyService.findAll());
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("statuses", Status.values());
        return "form";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public String updateProject(
            @PathVariable Long id,
            @RequestParam String topic,
            @RequestParam String description,
            @RequestParam(required = false) List<Long> labelIds,
            @RequestParam(required = false) List<Long> technologyIds,
            @RequestParam(required = false, defaultValue = "") String newLabels,
            @RequestParam(required = false, defaultValue = "") String newTechnologies,
            @RequestParam String course,
            @RequestParam Long responsibleTeacherId,
            @RequestParam(required = false) Long extendedFromId,
            @RequestParam String repoLink,
            @RequestParam Status status) {

        if (extendedFromId != null && extendedFromId.equals(id)) {
            throw new IllegalArgumentException("A project cannot be extended from itself!");
        }

        Project existing = projectService.findById(id);

        List<Label> labels;
        if (labelIds != null && !labelIds.isEmpty()) {
            labels = labelIds.stream().map(labelService::findById).toList();
        } else if (!newLabels.isBlank()) {
            List<Label> tmp = new ArrayList<>();
            for (String name : newLabels.split(",")) {
                String trimmed = name.trim();
                if (!trimmed.isEmpty()) {
                    Label l = new Label();
                    l.setName(trimmed);
                    tmp.add(labelService.save(l));
                }
            }
            labels = tmp;
        } else {
            labels = existing.getLabels();
        }

        List<Technology> technologies;
        if (technologyIds != null && !technologyIds.isEmpty()) {
            technologies = technologyIds.stream().map(technologyService::findById).toList();
        } else if (!newTechnologies.isBlank()) {
            List<Technology> tmp = new ArrayList<>();
            for (String name : newTechnologies.split(",")) {
                String trimmed = name.trim();
                if (!trimmed.isEmpty()) {
                    Technology t = new Technology();
                    t.setTechnologyName(trimmed);
                    tmp.add(technologyService.save(t));
                }
            }
            technologies = tmp;
        } else {
            technologies = existing.getTechnologies();
        }

        Project extendedFrom = (extendedFromId != null) ? projectService.findById(extendedFromId)
                : existing.getExtendedFrom();

        AppUser responsibleTeacher = userRepository.findById(responsibleTeacherId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid teacher ID"));

        projectService.update(id, topic, description, labels, technologies,
                course, responsibleTeacher, extendedFrom, repoLink, status);

        return "redirect:/projects";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public String deleteProject(@PathVariable Long id) {
        projectService.deleteById(id);
        return "redirect:/projects";
    }

    @GetMapping("/approve/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public String approveProject(@PathVariable Long id){
        projectService.approve(id);
        return "redirect:/projects";
    }

    @GetMapping("/reject/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public String rejectProject(@PathVariable Long id){
        projectService.reject(id);
        return "redirect:/projects";
    }

    @PostMapping("/submit/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public String submitProject(@PathVariable Long id) {
        projectService.submit(id);
        return "redirect:/projects";
    }

    @PostMapping("/cancel/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public String cancelSubmission(@PathVariable Long id) {
        projectService.cancel(id);
        return "redirect:/projects";
    }
}