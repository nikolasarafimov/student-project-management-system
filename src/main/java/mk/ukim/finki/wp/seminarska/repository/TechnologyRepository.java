package mk.ukim.finki.wp.seminarska.repository;

import mk.ukim.finki.wp.seminarska.model.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnologyRepository extends JpaRepository<Technology,Long> { }