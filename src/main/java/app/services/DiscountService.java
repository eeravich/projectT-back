package app.services;

import app.InvalidDataException;
import app.generated.jooq.tables.pojos.Discount;
import app.repository.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.tools.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DiscountService {
    private final DiscountRepository repository;

    public List<Discount> getList() {
        return repository.getList();
    }

    public Discount getById(Long discountId) {
        return repository.getById(discountId);
    }

    public void deleteDiscount(Long discountId) {
        repository.deleteDiscount(discountId);
    }

    public void createNewDiscount(Discount discount) {
        validateCreateDiscount(discount);
        discount.setDiscountId(repository.getNextDiscountId());
        repository.createDiscount(discount);
    }

    public void editDiscount(Discount discount) {
        validateCreateDiscount(discount);
        Discount oldDiscount = repository.getById(discount.getDiscountId());
        deleteDiscount(oldDiscount.getDiscountId());
        repository.createDiscount(discount);
    }

    public void validateCreateDiscount(Discount discount) {
        Map<String, String> errors = new HashMap<>();
        if (StringUtils.isBlank(discount.getName())) {
            errors.put("name", "Name can't be null");
        }
        if (StringUtils.isBlank(discount.getDescription())) {
            errors.put("description", "Description can't be null");
        }
        if (discount.getPercent() == null) {
            errors.put("percent", "Percent can't be null");
        }
        if (discount.getStartDatetime() != null && discount.getEndDatetime() != null) {
            if (discount.getStartDatetime().isAfter(discount.getEndDatetime())) {
                errors.put("startDate", "Start date can't be after end date");
            }
        }

        if (!errors.isEmpty()) {
            throw new InvalidDataException(errors);
        }
    }
}
