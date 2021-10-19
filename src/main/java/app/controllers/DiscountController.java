package app.controllers;

import app.entities.enums.Roles;
import app.generated.jooq.tables.pojos.Discount;
import app.services.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
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
    @Secured({Roles.Fields.ROLE_MANAGER, Roles.Fields.ROLE_ADMIN})
    @Transactional
    public void createDiscount(Discount discount) {
        service.createNewDiscount(discount);
    }

    @PutMapping("/edit")
    @Secured({Roles.Fields.ROLE_MANAGER, Roles.Fields.ROLE_ADMIN})
    @Transactional
    public void editDiscount(Discount discount) {
        service.editDiscount(discount);
    }

    @DeleteMapping("/delete/{discountId}")
    @Secured({Roles.Fields.ROLE_MANAGER, Roles.Fields.ROLE_ADMIN})
    @Transactional
    public void deleteDiscount(@PathVariable Long discountId) {
        service.deleteDiscount(discountId);
    }
}
