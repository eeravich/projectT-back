package app.controllers;

import app.entities.enums.Roles;
import app.generated.jooq.tables.pojos.Account;
import app.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService service;

    @GetMapping("/list")
    @Secured({Roles.Fields.ROLE_ADMIN, Roles.Fields.ROLE_MANAGER})
    public List<Account> getList() {
        return service.getList();
    }

    @GetMapping("/{accountId}")
    @Secured({Roles.Fields.ROLE_ADMIN, Roles.Fields.ROLE_MANAGER})
    public Account getById(@PathVariable Long accountId) {
        return service.getById(accountId);
    }

    @PostMapping("/create")
    @Secured({Roles.Fields.ROLE_ADMIN, Roles.Fields.ROLE_MANAGER})
    @Transactional
    public void createAccount(@RequestBody Account account) {
        service.createAccount(account);
    }

    @PutMapping("/edit")
    @Secured({Roles.Fields.ROLE_ADMIN, Roles.Fields.ROLE_MANAGER})
    @Transactional
    public void editAccount(@RequestBody Account account) {
        service.editAccount(account);
    }

    @DeleteMapping("/delete/{accountId}")
    @Secured({Roles.Fields.ROLE_ADMIN, Roles.Fields.ROLE_MANAGER})
    @Transactional
    public void deleteAccount(@PathVariable Long accountId) {
        service.deleteAccount(accountId);
    }

    @GetMapping("/info")
    public Account getCurrentAccountInfo() {
        return service.getCurrentAccountInfo();
    }

    @PutMapping("/editCurrent")
    @Transactional
    public void editCurrentAccount(@RequestBody Account account) {
        service.editCurrentAccount(account);
    }

    @DeleteMapping("/deleteCurrent")
    @Transactional
    public void deleteCurrentAccount() {
        service.deleteCurrentAccount();
    }
}
