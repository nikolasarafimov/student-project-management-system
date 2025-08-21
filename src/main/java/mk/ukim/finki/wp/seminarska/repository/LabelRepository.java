package mk.ukim.finki.wp.seminarska.repository;

import mk.ukim.finki.wp.seminarska.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelRepository extends JpaRepository<Label,Long> {
    List<Label> findByNameIn(List<String> names);
    @Query("SELECT l FROM Label l WHERE l.id IN (SELECT MIN(l2.id) FROM Label l2 GROUP BY l2.name)")
    List<Label> findDistinctByName();
}