package app.services;

import app.InvalidDataException;
import app.UserContext;
import app.entities.enums.Roles;
import app.generated.jooq.tables.pojos.Account;
import app.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class AccountServiceTest {
    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private UserContext userContext;

    @Autowired
    private AccountService accountService;

    @Test
    void getById() {
        Account user_account = new Account(
                1L,
                1L,
                LocalDateTime.now(),
                null,
                "test_user",
                "pass",
                "user@mail.er",
                "Ivan",
                "Demkin",
                "+739392904332",
                Roles.ROLE_USER.getId().longValue(),
                false
        );
        Long accountId = user_account.getAccountId();

        when(accountRepository.getActualAccountById(accountId)).thenReturn(user_account);

        assertEquals(user_account, accountService.getById(accountId));
    }

    @Test
    void createAccountTest() {
        Account user_account = new Account(
                1L,
                1L,
                LocalDateTime.now(),
                null,
                "test_user",
                "pass",
                "user@mail.er",
                "Ivan",
                "Demkin",
                "+739392904332",
                Roles.ROLE_USER.getId().longValue(),
                false
        );
        doNothing().when(accountRepository).createAccount(any(Account.class));

        accountService.createAccount(user_account);
        verify(accountRepository, times(1)).createAccount(user_account);
    }

    @Test
    void editAccountTest() {
        Account user_account = new Account(
                1L,
                1L,
                LocalDateTime.now(),
                null,
                "test_user",
                "pass",
                "user@mail.er",
                "Ivan",
                "Demkin",
                "+739392904332",
                Roles.ROLE_USER.getId().longValue(),
                false
        );
        doNothing().when(accountRepository).createAccount(any(Account.class));
        doNothing().when(accountRepository).deleteAccount(any(Long.class));
        when(accountRepository.getActualAccountById(any(Long.class))).thenReturn(user_account);

        accountService.editAccount(user_account);
        verify(accountRepository, times(1)).getActualAccountById(user_account.getAccountId());
        verify(accountRepository, times(1)).deleteAccount(user_account.getAccountId());
        verify(accountRepository, times(1)).createAccount(user_account);
    }

    @Test
    void deleteAccountTest() {
        Long accountId = 1L;
        doNothing().when(accountRepository).deleteAccount(any(Long.class));

        accountService.deleteAccount(accountId);
        verify(accountRepository, times(1)).deleteAccount(accountId);
    }


    @Test
    void validateCreateAccountWithNullFields() {
        Account testAcc = new Account();

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> accountService.validateCreateAccount(testAcc));

        assertNotNull(exception.getErrors());
        assertTrue(exception.getErrors().containsKey("login"));
        assertTrue(exception.getErrors().containsKey("password"));
        assertTrue(exception.getErrors().containsKey("email"));
        assertTrue(exception.getErrors().containsKey("phone"));
        assertTrue(exception.getErrors().containsKey("name"));
        assertTrue(exception.getErrors().containsKey("surname"));
        assertEquals(6, exception.getErrors().size());
    }

    @Test
    void validateCreateAccountWithEmptyFields() {
        Account testAcc = new Account();
        testAcc.setLogin("");
        testAcc.setPassword("");
        testAcc.setEmail("");
        testAcc.setName("");
        testAcc.setSurname("");
        testAcc.setPhone("");

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> accountService.validateCreateAccount(testAcc));

        assertNotNull(exception.getErrors());
        assertTrue(exception.getErrors().containsKey("login"));
        assertTrue(exception.getErrors().containsKey("password"));
        assertTrue(exception.getErrors().containsKey("email"));
        assertTrue(exception.getErrors().containsKey("phone"));
        assertTrue(exception.getErrors().containsKey("name"));
        assertTrue(exception.getErrors().containsKey("surname"));
        assertEquals(6, exception.getErrors().size());
    }

    @Test
    void validateCreateAccountWithBlankFields() {
        Account testAcc = new Account();
        testAcc.setLogin(" ");
        testAcc.setPassword(" ");
        testAcc.setEmail(" ");
        testAcc.setName("  ");
        testAcc.setSurname("  ");
        testAcc.setPhone(" ");

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> accountService.validateCreateAccount(testAcc));

        assertNotNull(exception.getErrors());
        assertTrue(exception.getErrors().containsKey("login"));
        assertTrue(exception.getErrors().containsKey("email"));
        assertTrue(exception.getErrors().containsKey("phone"));
        assertTrue(exception.getErrors().containsKey("name"));
        assertTrue(exception.getErrors().containsKey("surname"));
        assertFalse(exception.getErrors().containsKey("password"));
        assertEquals(5, exception.getErrors().size());

    }

    @Test
    void validateCreateAccountWithExistingLoginEmailPhone() {
        Account testAcc = new Account();
        testAcc.setLogin("test");
        testAcc.setPassword("1234");
        testAcc.setEmail("test@mail.ru");
        testAcc.setName("Ivan");
        testAcc.setSurname("Pupishev");
        testAcc.setPhone("+12345678");

        when(accountRepository.isExistByLogin(any(String.class))).thenReturn(true);
        when(accountRepository.isExistByEmail(any(String.class))).thenReturn(true);
        when(accountRepository.isExistByPhone(any(String.class))).thenReturn(true);

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> accountService.validateCreateAccount(testAcc));

        assertNotNull(exception.getErrors());
        assertTrue(exception.getErrors().containsKey("login"));
        assertTrue(exception.getErrors().containsKey("email"));
        assertTrue(exception.getErrors().containsKey("phone"));
        assertFalse(exception.getErrors().containsKey("name"));
        assertFalse(exception.getErrors().containsKey("surname"));
        assertFalse(exception.getErrors().containsKey("password"));
        assertEquals(3, exception.getErrors().size());

    }

    @Test
    void editCurrentAccountTest_TryEditForeign() {
        Account user_account = new Account(
                1L,
                1L,
                LocalDateTime.now(),
                null,
                "test_user",
                "pass",
                "user@mail.er",
                "Ivan",
                "Demkin",
                "+739392904332",
                Roles.ROLE_USER.getId().longValue(),
                false
        );
        when(userContext.getAccountId()).thenReturn(user_account.getAccountId() + 1);

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> accountService.editCurrentAccount(user_account));
        assertNotNull(ex);
//        assertTrue(ex.getMessage().contains("Accounts mismatch"));

    }

    @Test
    void registerNewAccountTest_TryRegisterWhileLoggedIn() {
        Account user_account = new Account(
                1L,
                1L,
                LocalDateTime.now(),
                null,
                "test_user",
                "pass",
                "user@mail.er",
                "Ivan",
                "Demkin",
                "+739392904332",
                Roles.ROLE_USER.getId().longValue(),
                false
        );
        when(userContext.getAccountId()).thenReturn(user_account.getAccountId());

        InvalidDataException ex = assertThrows(InvalidDataException.class, () -> accountService.registerNewAccount(user_account));
        assertNotNull(ex);
//        assertTrue(ex.getMessage().contains("Exit from account and try again"));
    }

}