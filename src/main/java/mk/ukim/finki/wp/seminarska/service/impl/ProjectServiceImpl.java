package mk.ukim.finki.wp.seminarska.service.impl;

import mk.ukim.finki.wp.seminarska.model.*;
import mk.ukim.finki.wp.seminarska.repository.ProjectRepository;
import mk.ukim.finki.wp.seminarska.service.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public List<Project> findAll() { return projectRepository.findAll(); }

    @Override
    public Project findById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Project not found: " + id));
    }

    @Override
    public Project save(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Project save(String topic, String description, List<Label> labels, List<Technology> technologies, String course, AppUser responsibleTeacher, Project extendedFrom, String repoLink, Status status) {
        Project project = new Project(topic, description, labels, technologies, course, responsibleTeacher, extendedFrom, repoLink, status);
        return projectRepository.save(project);
    }

    @Override
    public Project update(Long id, String topic, String description, List<Label> labels, List<Technology> technologies, String course, AppUser responsibleTeacher, Project extendedFrom, String repoLink, Status status) {
        Project existing = findById(id);
        if (existing.getStatus() == Status.SUBMITTED || existing.getStatus() == Status.APPROVED) {
            throw new IllegalStateException("Cannot edit a submitted or approved project!");
        }
        existing.setTopic(topic);
        existing.setDescription(description);
        existing.setLabels(labels);
        existing.setTechnologies(technologies);
        existing.setCourse(course);
        existing.setResponsibleTeacher(responsibleTeacher);
        existing.setExtendedFrom(extendedFrom);
        existing.setRepoLink(repoLink);
        existing.setStatus(status);

        return projectRepository.save(existing);
    }

    @Override
    public Project update(Long id, Project project) {
        Project existing = findById(id);
        if (existing.getStatus() == Status.SUBMITTED || existing.getStatus() == Status.APPROVED) {
            throw new IllegalStateException("Cannot edit a submitted or approved project!");
        }
        existing.setTopic(project.getTopic());
        existing.setDescription(project.getDescription());
        existing.setLabels(project.getLabels());
        existing.setTechnologies(project.getTechnologies());
        existing.setCourse(project.getCourse());
        existing.setResponsibleTeacher(project.getResponsibleTeacher());
        existing.setExtendedFrom(project.getExtendedFrom());
        existing.setRepoLink(project.getRepoLink());
        existing.setStatus(project.getStatus());

        return projectRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteById(Long projectId) {
        List<Project> children = projectRepository.findByExtendedFromId(projectId);

        if (!children.isEmpty()) {
            for (Project child : children) {
                child.setExtendedFrom(null);
                projectRepository.save(child);
            }
            // throw new IllegalStateException("Cannot delete project, other projects extend from it");
        }
        projectRepository.deleteById(projectId);
    }

    @Override
    public List<Project> findBySearchText(String text) {
        return projectRepository.findBySearchText(text);
    }

    @Override
    public void submit(Long id) {
        Project p = findById(id);
        if (p.getStatus() != Status.DRAFT && p.getStatus() != Status.REJECTED) {
            throw new IllegalStateException("Only DRAFT or REJECTED projects can be submitted!");
        }
        p.setStatus(Status.SUBMITTED);
        projectRepository.save(p);
    }

    @Override
    public void approve(Long id) {
        Project p = findById(id);
        if (p.getStatus() != Status.SUBMITTED) throw new IllegalStateException("Only SUBMITTED can be approved!");
        p.setStatus(Status.APPROVED);
        projectRepository.save(p);
    }

    @Override
    public void reject(Long id) {
        Project p = findById(id);
        if (p.getStatus() != Status.SUBMITTED) throw new IllegalStateException("Only SUBMITTED can be rejected.");
        p.setStatus(Status.REJECTED);
        projectRepository.save(p);
    }

    @Override
    public void cancel(Long id) {
        Project p = findById(id);
        if (p.getStatus() != Status.SUBMITTED) {
            throw new IllegalStateException("Only SUBMITTED projects can be cancelled.");
        }
        p.setStatus(Status.DRAFT);
        projectRepository.save(p);
    }
}