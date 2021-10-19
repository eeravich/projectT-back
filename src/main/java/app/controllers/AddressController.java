package app.controllers;

import app.entities.enums.Roles;
import app.generated.jooq.tables.pojos.Address;
import app.services.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService service;

    @GetMapping("/list")
    @Secured({Roles.Fields.ROLE_MANAGER, Roles.Fields.ROLE_ADMIN})
    public List<Address> getList() {
        return service.getList();
    }

    @GetMapping("/listByUser")
    public List<Address> getListByUser() {
        return service.getList();
    }

    @GetMapping("/{addressId}")
    public Address getById(@PathVariable Long addressId) {
        return service.getById(addressId);
    }

    @PostMapping("/create")
    @Transactional
    public void createAddress(Address address) {
        service.createAddress(address);
    }

    @PutMapping("/edit")
    @Transactional
    public void editAddress(Address address) {
        service.editAddress(address);
    }

    @DeleteMapping("/delete/{addressId}")
    @Transactional
    public void deleteAddress(@PathVariable Long addressId) {
        service.deleteAddress(addressId);
    }
}
