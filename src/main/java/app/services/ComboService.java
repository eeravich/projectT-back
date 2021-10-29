package app.services;

import app.InvalidDataException;
import app.entities.pojos.ComboPojo;
import app.generated.jooq.tables.pojos.Combo;
import app.repository.ComboRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.tools.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ComboService {
    private final ComboRepository repository;

    public List<ComboPojo> getList() {
        return repository.getList();
    }

    public ComboPojo getById(Long comboId) {
        ComboPojo comboPojo = repository.getById(comboId);
        comboPojo.setDiscounts(repository.getDiscountsByComboId(comboId));
        return comboPojo;
    }

    public void createCombo(Combo combo) {
        validateCreateCombo(combo);
        combo.setComboId(repository.getNextComboId());
        repository.createCombo(combo);
    }

    public void deleteCombo(Long comboId) {
        repository.deleteCombo(comboId);
    }

    public void editCombo(Combo combo) {
        validateCreateCombo(combo);
        Combo oldCombo = repository.getById(combo.getComboId());
        deleteCombo(oldCombo.getComboId());
        repository.createCombo(combo);
    }

    private void validateCreateCombo(Combo combo) {
        Map<String, String> errors = new HashMap<>();
        if (StringUtils.isBlank(combo.getName())) {
            errors.put("name", "Name can't be null");
        }
        if (StringUtils.isBlank(combo.getDescription())) {
            errors.put("description", "Description can't be null");
        }
        if (combo.getPrice() == null) {
            errors.put("price", "Price can't be null");
        } else if (combo.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            errors.put("price", "Price can't be less than zero");
        }

        if (!errors.isEmpty()) {
            throw new InvalidDataException(errors);
        }
    }
}
