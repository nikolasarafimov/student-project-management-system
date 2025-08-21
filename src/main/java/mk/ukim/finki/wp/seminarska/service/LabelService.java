package mk.ukim.finki.wp.seminarska.service;

import mk.ukim.finki.wp.seminarska.model.Label;
import java.util.List;

public interface LabelService {
    List<Label> findAll();
    Label findById(Long id);
    Label save(Label label);
    Label update(Long id, Label label);
    void deleteById(Long id);
    List<Label> findDistinctLabelsByName();
}