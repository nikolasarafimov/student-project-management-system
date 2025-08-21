package mk.ukim.finki.wp.seminarska.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topic;

    @Column(length = 2000)
    private String description;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "project_label", joinColumns = @JoinColumn(name = "project_id"), inverseJoinColumns = @JoinColumn(name = "label_id"))
    private List<Label> labels;

    @ManyToMany
    @JoinTable(name = "project_technology", joinColumns = @JoinColumn(name = "project_id"), inverseJoinColumns = @JoinColumn(name = "technology_id"))
    private List<Technology> technologies;

    private String course;

    @ManyToOne
    @JoinColumn(name = "responsible_teacher_id")
    private AppUser responsibleTeacher; // changed from String

    @ManyToOne
    @JoinColumn(name = "extended_from_id")
    private Project extendedFrom;

    private String repoLink;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Project(String topic, String description, List<Label> labels, List<Technology> technologies,
                   String course, AppUser responsibleTeacher, Project extendedFrom, String repoLink, Status status) {
        this.topic = topic;
        this.description = description;
        this.labels = labels;
        this.technologies = technologies;
        this.course = course;
        this.responsibleTeacher = responsibleTeacher;
        this.extendedFrom = extendedFrom;
        this.repoLink = repoLink;
        this.status = status;
    }

    public Long getId() { return id; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Label> getLabels() { return labels; }
    public void setLabels(List<Label> labels) { this.labels = labels; }

    public List<Technology> getTechnologies() { return technologies; }
    public void setTechnologies(List<Technology> technologies) { this.technologies = technologies; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public AppUser getResponsibleTeacher() { return responsibleTeacher; }
    public void setResponsibleTeacher(AppUser responsibleTeacher) { this.responsibleTeacher = responsibleTeacher; }

    public Project getExtendedFrom() { return extendedFrom; }
    public void setExtendedFrom(Project extendedFrom) { this.extendedFrom = extendedFrom; }

    public String getRepoLink() { return repoLink; }
    public void setRepoLink(String repoLink) { this.repoLink = repoLink; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Project() {}
}