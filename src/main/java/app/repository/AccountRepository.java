package app.repository;

import app.entities.pojos.AccountPojo;
import app.generated.jooq.Sequences;
import app.generated.jooq.tables.pojos.Account;
import app.generated.jooq.tables.pojos.Role;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static app.generated.jooq.Tables.ACCOUNT;
import static app.generated.jooq.Tables.ROLE;

@Repository
@RequiredArgsConstructor
public class AccountRepository {
    private final DSLContext dslContext;

    public Account getById(Long id) {
        return dslContext.select(
                    ACCOUNT.ID,
                    ACCOUNT.ACCOUNT_ID,
                    ACCOUNT.DELETED_DATETIME,
                    ACCOUNT.DELETED_DATETIME,
                    ACCOUNT.EMAIL,
                    ACCOUNT.LOGIN,
                    ACCOUNT.PHONE,
                    ACCOUNT.NAME,
                    ACCOUNT.SURNAME,
                    ACCOUNT.ROLE_ID,
                    ACCOUNT.IS_BLOCKED
                )
                .from(ACCOUNT)
                .where(ACCOUNT.ID.eq(id))
                .fetchOneInto(Account.class);
    }

    public void createAccount(Account account) {
        dslContext.insertInto(ACCOUNT)
                .set(ACCOUNT.ACCOUNT_ID, account.getAccountId())
                .set(ACCOUNT.CREATED_DATETIME, LocalDateTime.now())
                .set(ACCOUNT.LOGIN, account.getLogin())
                .set(ACCOUNT.PASSWORD, account.getPassword())
                .set(ACCOUNT.EMAIL, account.getEmail())
                .set(ACCOUNT.NAME, account.getName())
                .set(ACCOUNT.SURNAME, account.getSurname())
                .set(ACCOUNT.PHONE, account.getPhone())
                .set(ACCOUNT.ROLE_ID, account.getRoleId())
                .set(ACCOUNT.IS_BLOCKED, account.getIsBlocked())
                .execute();
    }

    public Account getActualAccountById(Long accountId) {
        return dslContext.select(
                        ACCOUNT.ID,
                        ACCOUNT.ACCOUNT_ID,
                        ACCOUNT.DELETED_DATETIME,
                        ACCOUNT.DELETED_DATETIME,
                        ACCOUNT.EMAIL,
                        ACCOUNT.LOGIN,
                        ACCOUNT.PHONE,
                        ACCOUNT.NAME,
                        ACCOUNT.SURNAME,
                        ACCOUNT.ROLE_ID,
                        ACCOUNT.IS_BLOCKED
                )
                .from(ACCOUNT)
                .where(ACCOUNT.ACCOUNT_ID.eq(accountId), ACCOUNT.DELETED_DATETIME.isNull())
                .fetchOneInto(Account.class);
    }

    public Long getNextAccountId() {
        return dslContext.nextval(Sequences.ACCOUNT_ACCOUNT_ID_SEQ);
    }

    public void deleteAccount(Long accountId) {
        dslContext.update(ACCOUNT)
                .set(ACCOUNT.DELETED_DATETIME, LocalDateTime.now())
                .where(ACCOUNT.ACCOUNT_ID.eq(accountId), ACCOUNT.DELETED_DATETIME.isNull())
                .execute();
    }

    public List<Account> getActualList() {
        return dslContext.selectFrom(ACCOUNT)
                .where(ACCOUNT.DELETED_DATETIME.isNull())
                .fetchInto(Account.class);
    }

    public AccountPojo findByLogin(String login) {
        return dslContext.select(ACCOUNT.fields())
                .select(ROLE.NAME.as("roleName"))
                .from(ACCOUNT)
                .leftJoin(ROLE).on(ACCOUNT.ROLE_ID.eq(ROLE.ID))
                .where(ACCOUNT.LOGIN.eq(login), ACCOUNT.DELETED_DATETIME.isNull())
                .fetchOneInto(AccountPojo.class);
    }

    public Account findByPhone(String phone) {
        return dslContext.selectFrom(ACCOUNT)
                .where(ACCOUNT.PHONE.eq(phone), ACCOUNT.DELETED_DATETIME.isNull())
                .fetchOneInto(Account.class);
    }

    public Account findByEmail(String email) {
        return dslContext.selectFrom(ACCOUNT)
                .where(ACCOUNT.EMAIL.eq(email), ACCOUNT.DELETED_DATETIME.isNull())
                .fetchOneInto(Account.class);
    }

    public boolean isExistByLogin(String login) {
        return dslContext.fetchExists(
                dslContext.selectFrom(ACCOUNT)
                        .where(ACCOUNT.LOGIN.eq(login), ACCOUNT.DELETED_DATETIME.isNull())
        );
    }

    public boolean isExistByPhone(String phone) {
        return dslContext.fetchExists(
                dslContext.selectFrom(ACCOUNT)
                        .where(ACCOUNT.PHONE.eq(phone), ACCOUNT.DELETED_DATETIME.isNull())
        );
    }

    public boolean isExistByEmail(String email) {
        return dslContext.fetchExists(
                dslContext.selectFrom(ACCOUNT)
                        .where(ACCOUNT.EMAIL.eq(email), ACCOUNT.DELETED_DATETIME.isNull())
        );
    }
}
