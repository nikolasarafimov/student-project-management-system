package mk.ukim.finki.wp.seminarska.service;

import mk.ukim.finki.wp.seminarska.model.Technology;

import java.util.List;

public interface TechnologyService {
    List<Technology> findAll();
    Technology findById(Long id);
    Technology save(Technology technology);
    Technology update(Long id, Technology technology);
    void deleteById(Long id);
}