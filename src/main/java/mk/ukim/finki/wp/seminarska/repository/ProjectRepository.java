
package mk.ukim.finki.wp.seminarska.repository;

import mk.ukim.finki.wp.seminarska.model.Label;
import mk.ukim.finki.wp.seminarska.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
    List<Project> findByTopicContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String topic, String description);
    List<Project> findDistinctByLabelsIn(List<Label> labels);
    List<Project> findDistinctByLabels_IdIn(Collection<Long> labelIds);

    List<Project> findByExtendedFromId(Long projectId);
    @Query("""
    SELECT DISTINCT p
    FROM Project p
    LEFT JOIN p.labels l
    WHERE LOWER(p.topic) LIKE LOWER(CONCAT('%', :text, '%'))
        OR LOWER(p.description) LIKE LOWER(CONCAT('%', :text, '%'))
        OR LOWER(l.name) LIKE LOWER(CONCAT('%', :text, '%'))
    """)
    List<Project> findBySearchText(@Param("text") String text);
}