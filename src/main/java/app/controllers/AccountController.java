package app.controllers;

import app.generated.jooq.tables.pojos.Account;
import app.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService service;

    @GetMapping("/list")
    public List<Account> getList() {
        return service.getList();
    }

    @GetMapping("/{accountId}")
    public Account getById(@PathVariable Long accountId) {
        return service.getById(accountId);
    }

    @PostMapping("/create")
    @Transactional
    public void createAccount(@RequestBody Account account) {
        service.createAccount(account);
    }

    @PutMapping("/edit")
    @Transactional
    public void editAccount(@RequestBody Account account) {
        service.editAccount(account);
    }

    @DeleteMapping("/delete/{accountId}")
    @Transactional
    public void deleteAccount(@PathVariable Long accountId) {
        service.deleteAccount(accountId);
    }
}
