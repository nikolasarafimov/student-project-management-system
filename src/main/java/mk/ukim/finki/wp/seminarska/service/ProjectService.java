package mk.ukim.finki.wp.seminarska.service;

import mk.ukim.finki.wp.seminarska.model.*;

import java.util.List;

public interface ProjectService {
    List<Project> findAll();
    Project findById(Long id);
    Project save(Project project);
    Project save(String topic, String description, List<Label> labels, List<Technology> technologies, String course, AppUser responsibleTeacher, Project extendedFrom, String repoLink, Status status);
    Project update(Long id,String topic, String description, List<Label> labels, List<Technology> technologies, String course, AppUser responsibleTeacher, Project extendedFrom, String repoLink, Status status);

    Project update(Long id, Project project);
    void deleteById(Long id);

    public List<Project> findBySearchText(String text);
    void submit(Long id);
    void approve(Long id);
    void reject(Long id);
    void cancel(Long id);
}