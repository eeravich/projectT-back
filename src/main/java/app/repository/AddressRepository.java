package app.repository;

import app.Utils;
import app.generated.projectT.Sequences;
import app.generated.projectT.tables.daos.AddressDao;
import app.generated.projectT.tables.pojos.Address;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static app.generated.projectT.Tables.ADDRESS;

@Repository
@RequiredArgsConstructor
public class AddressRepository {
    private final DSLContext dslContext;
    private final AddressDao addressDao;

    public List<Address> getList() {
        return dslContext.selectFrom(ADDRESS)
                .where(ADDRESS.DELETED_DATETIME.isNull())
                .fetchInto(Address.class);
    }

    public List<Address> getListByUser(Long accountEntityId) {
        return dslContext.selectFrom(ADDRESS)
                .where(
                        ADDRESS.ACCOUNT_ENTITY_ID.eq(accountEntityId),
                        ADDRESS.DELETED_DATETIME.isNull()
                )
                .fetchInto(Address.class);
    }

    public Optional<Address> getActualAddress(Long addressEntityId) {
        return dslContext.selectFrom(ADDRESS)
                .where(
                        ADDRESS.ENTITY_ID.eq(addressEntityId),
                        ADDRESS.DELETED_DATETIME.isNull()
                )
                .fetchOptionalInto(Address.class);
    }

    public void createAddress(Address address) {
        addressDao.insert(address);
    }

    public void deleteAddress(Long addressId) {
        dslContext.update(ADDRESS)
                .set(ADDRESS.DELETED_DATETIME, Utils.now())
                .where(ADDRESS.ID.eq(addressId))
                .execute();
    }

    public Long getNextAddressId() {
        return dslContext.nextval(Sequences.ADDRESS_ID_SEQ);
    }
}
