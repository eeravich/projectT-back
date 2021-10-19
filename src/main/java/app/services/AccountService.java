package app.services;

import app.InvalidDataException;
import app.UserContext;
import app.entities.enums.Roles;
import app.generated.jooq.tables.pojos.Account;
import app.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.tools.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserContext userContext;

    public Account getById(Long accountId) {
        return repository.getActualAccountById(accountId);
    }

    public void createAccount(Account account) {
        validateCreateAccount(account);
        account.setAccountId(repository.getNextAccountId());
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        if (account.getIsBlocked() == null) {
            account.setIsBlocked(false);
        }
        repository.createAccount(account);
    }

    public void editAccount(Account account) {
        validateCreateAccount(account);
        Account oldAccount = repository.getActualAccountById(account.getAccountId());
        if (account.getIsBlocked() == null) {
            account.setIsBlocked(false);
        }
        deleteAccount(oldAccount.getAccountId());
        repository.createAccount(account);
    }

    public void deleteAccount(Long accountId) {
        repository.deleteAccount(accountId);
    }

    public void validateCreateAccount(Account account) {
        Map<String, String> errors = new HashMap<>();
        if (StringUtils.isBlank(account.getLogin())) {
            errors.put("login", "Login can't be null");
        } else if (repository.isExistByLogin(account.getLogin())) {
            errors.put("login", "User with this login already exist");
        }
        if (StringUtils.isEmpty(account.getPassword())) {
            errors.put("password", "Password can't be null");
        }
        if (StringUtils.isBlank(account.getEmail())) {
            errors.put("email", "Email can't be null");
        } else if (repository.isExistByEmail(account.getEmail())) {
            errors.put("email", "User with this email already exist");
        }
        if (StringUtils.isBlank(account.getPhone())) {
            errors.put("phone", "Phone can't be null");
        } else if (repository.isExistByPhone(account.getPhone())) {
            errors.put("phone", "User with this phone already exist");
        }
        if (StringUtils.isBlank(account.getName())) {
            errors.put("name", "Name can't be null");
        }
        if (StringUtils.isBlank(account.getSurname())) {
            errors.put("surname", "Surname can't be null");
        }

        if (!errors.isEmpty()) {
            throw new InvalidDataException(errors);
        }
    }

    public List<Account> getList() {
        return repository.getActualList();
    }

    public Account getCurrentAccountInfo() {
        return repository.getActualAccountById(userContext.getAccountId());
    }

    public void editCurrentAccount(Account account) {
        if (!userContext.getAccountId().equals(account.getAccountId())) {
            throw new InvalidDataException("Accounts mismatch!");
        }
        editAccount(account);
    }

    public void registerNewAccount(Account account) {
        if (userContext.getAccountId() == null) {
            account.setRoleId(Roles.ROLE_USER.getId().longValue());
            createAccount(account);
        } else {
            throw new InvalidDataException("Exit from account and try again");
        }
    }

    public void deleteCurrentAccount() {
        deleteAccount(userContext.getAccountId());
    }

}
