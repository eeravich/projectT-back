package app.controllers;

import app.generated.jooq.tables.pojos.Discount;
import app.services.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/discount")
@RequiredArgsConstructor
public class DiscountController {
    private final DiscountService service;

    @GetMapping("/list")
    public List<Discount> getList() {
        return service.getList();
    }

    @GetMapping("/{discountId}")
    public Discount getById(@PathVariable Long discountId) {
        return service.getById(discountId);
    }

    @PostMapping("/create")
    @Transactional
    public void createDiscount(Discount discount) {
        service.createNewDiscount(discount);
    }

    @PutMapping("/edit")
    @Transactional
    public void editDiscount(Discount discount) {
        service.editDiscount(discount);
    }

    @DeleteMapping("/delete/{discountId}")
    @Transactional
    public void deleteDiscount(@PathVariable Long discountId) {
        service.deleteDiscount(discountId);
    }
}
