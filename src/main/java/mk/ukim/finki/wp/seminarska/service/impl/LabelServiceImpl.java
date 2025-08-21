package mk.ukim.finki.wp.seminarska.service.impl;

import mk.ukim.finki.wp.seminarska.model.Label;
import mk.ukim.finki.wp.seminarska.model.Project;
import mk.ukim.finki.wp.seminarska.repository.LabelRepository;
import mk.ukim.finki.wp.seminarska.repository.ProjectRepository;
import mk.ukim.finki.wp.seminarska.service.LabelService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelServiceImpl implements LabelService{

    private final LabelRepository labelRepository;

    public LabelServiceImpl(LabelRepository labelRepository, ProjectRepository projectRepository) {
        this.labelRepository = labelRepository;
    }

    @Override
    public List<Label> findAll() {
        return labelRepository.findAll();
    }

    @Override
    public Label findById(Long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Label not found: " + id));
    }

    @Override
    public Label save(Label label) {
        return labelRepository.save(label);
    }

    @Override
    public Label update(Long id, Label label) {
        Label existing = findById(id);
        existing.setName(label.getName());
        return labelRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        labelRepository.deleteById(id);
    }

    @Override
    public List<Label> findDistinctLabelsByName() {
        return labelRepository.findDistinctByName();
    }
}