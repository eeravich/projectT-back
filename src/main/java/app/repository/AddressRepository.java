package app.repository;

import app.generated.jooq.Sequences;
import app.generated.jooq.tables.pojos.Address;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static app.generated.jooq.Tables.ADDRESS;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AddressRepository {
    private final DSLContext dslContext;

    public List<Address> getList() {
        return dslContext.selectFrom(ADDRESS)
                .where(ADDRESS.DELETED_DATETIME.isNull())
                .fetchInto(Address.class);
    }

    public List<Address> getListByUser(Long accountId) {
        return dslContext.selectFrom(ADDRESS)
                .where(ADDRESS.ACCOUNT_ID.eq(accountId), ADDRESS.DELETED_DATETIME.isNull())
                .fetchInto(Address.class);
    }

    public Address getById(Long addressId) {
        return dslContext.selectFrom(ADDRESS)
                .where(ADDRESS.ADDRESS_ID.eq(addressId), ADDRESS.DELETED_DATETIME.isNull())
                .fetchOneInto(Address.class);
    }

    public void createAddress(Address address) {
        dslContext.insertInto(ADDRESS)
                .set(ADDRESS.ADDRESS_ID, address.getAddressId())
                .set(ADDRESS.CREATED_DATETIME, LocalDateTime.now())
                .set(ADDRESS.STREET, address.getStreet())
                .set(ADDRESS.HOUSE, address.getHouse())
                .set(ADDRESS.APARTMENT, address.getApartment())
                .set(ADDRESS.ENTRANCE, address.getEntrance())
                .set(ADDRESS.FLOOR, address.getFloor())
                .set(ADDRESS.CODE, address.getCode())
                .set(ADDRESS.ACCOUNT_ID, address.getAccountId())
                .execute();
    }

    public void deleteAddress(Long addressId) {
        dslContext.update(ADDRESS)
                .set(ADDRESS.DELETED_DATETIME, LocalDateTime.now())
                .where(ADDRESS.ADDRESS_ID.eq(addressId), ADDRESS.DELETED_DATETIME.isNull())
                .execute();
    }

    public Long getNextAddressId() {
        return dslContext.nextval(Sequences.ADDRESS_ADDRESS_ID_SEQ);
    }
}
