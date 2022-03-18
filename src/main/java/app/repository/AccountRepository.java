package app.repository;

import app.Utils;
import app.generated.projectT.Sequences;
import app.generated.projectT.tables.daos.AccountDao;
import app.generated.projectT.tables.pojos.Account;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static app.generated.projectT.Tables.ACCOUNT;


@Repository
@RequiredArgsConstructor
public class AccountRepository {
    private final DSLContext dslContext;
    private final AccountDao accountDao;

    public Optional<Account> getById(Long id) {
        return dslContext.selectFrom(ACCOUNT)
                .where(ACCOUNT.ID.eq(id))
                .fetchOptionalInto(Account.class);
    }

    public void createAccount(Account account) {
        accountDao.insert(account);
    }

    public Optional<Account> getActualAccount(Long accountEntityId) {
        return dslContext.selectFrom(ACCOUNT)
                .where(
                        ACCOUNT.ENTITY_ID.eq(accountEntityId),
                        ACCOUNT.DELETED_DATETIME.isNull()
                )
                .fetchOptionalInto(Account.class);
    }

    public Long getNextId() {
        return dslContext.nextval(Sequences.ACCOUNT_ID_SEQ);
    }

    public void deleteAccount(Long accountId) {
        dslContext.update(ACCOUNT)
                .set(ACCOUNT.DELETED_DATETIME, Utils.now())
                .where(ACCOUNT.ID.eq(accountId))
                .execute();
    }

    public List<Account> getActualList() {
        return dslContext.selectFrom(ACCOUNT)
                .where(ACCOUNT.DELETED_DATETIME.isNull())
                .fetchInto(Account.class);
    }

    public Account findByLogin(String login) {
        return dslContext.selectFrom(ACCOUNT)
                .where(ACCOUNT.LOGIN.eq(login), ACCOUNT.DELETED_DATETIME.isNull())
                .fetchOneInto(Account.class);
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
