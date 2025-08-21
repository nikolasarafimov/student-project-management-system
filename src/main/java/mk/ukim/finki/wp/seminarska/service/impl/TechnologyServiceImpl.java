package mk.ukim.finki.wp.seminarska.service.impl;

import mk.ukim.finki.wp.seminarska.model.Technology;
import mk.ukim.finki.wp.seminarska.repository.TechnologyRepository;
import mk.ukim.finki.wp.seminarska.service.TechnologyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TechnologyServiceImpl implements TechnologyService {
    private final TechnologyRepository technologyRepository;

    public TechnologyServiceImpl(TechnologyRepository technologyRepository) {
        this.technologyRepository = technologyRepository;
    }

    @Override
    public List<Technology> findAll() {
        return technologyRepository.findAll();
    }

    @Override
    public Technology findById(Long id) {
        return technologyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Technology not found: " + id));
    }

    @Override
    public Technology save(Technology technology) {
        return technologyRepository.save(technology);
    }

    @Override
    public Technology update(Long id, Technology technology) {
        Technology existing = findById(id);
        existing.setTechnologyName(technology.getTechnologyName());
        return technologyRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        Technology deletedTechnology=this.findById(id);
        technologyRepository.delete(deletedTechnology);
    }
}