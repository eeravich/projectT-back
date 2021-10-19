package app.controllers;

import app.entities.DTO.AuthDTO;
import app.generated.jooq.tables.pojos.Account;
import app.services.AccountService;
import app.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authService;
    private final AccountService accountService;

    @PostMapping("/auth")
    public void auth(@RequestBody AuthDTO authDTO) {
        authService.auth(authDTO);
    }

    @PostMapping("/register")
    @Transactional
    public void register(@RequestBody Account account) {
        accountService.registerNewAccount(account);
    }
}
