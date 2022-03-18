package app.repository;

import app.Utils;
import app.generated.projectT.Sequences;
import app.generated.projectT.tables.daos.DiscountDao;
import app.generated.projectT.tables.pojos.Discount;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static app.generated.projectT.Tables.DISCOUNT;

@Repository
@RequiredArgsConstructor
public class DiscountRepository {
    private final DSLContext dslContext;
    private final DiscountDao discountDao;

    public List<Discount> getList() {
        return dslContext.selectFrom(DISCOUNT)
                .where(DISCOUNT.DELETED_DATETIME.isNull())
                .fetchInto(Discount.class);
    }

    public Optional<Discount> getActualDiscount(Long discountEntityId) {
        return dslContext.selectFrom(DISCOUNT)
                .where(
                        DISCOUNT.ENTITY_ID.eq(discountEntityId),
                        DISCOUNT.DELETED_DATETIME.isNull()
                )
                .fetchOptionalInto(Discount.class);
    }

    public void createDiscount(Discount discount) {
        discountDao.insert(discount);
    }

    public void deleteDiscount(Long discountId) {
        dslContext.update(DISCOUNT)
                .set(DISCOUNT.DELETED_DATETIME, Utils.now())
                .where(DISCOUNT.ID.eq(discountId))
                .execute();
    }

    public Long getNextDiscountId() {
        return dslContext.nextval(Sequences.DISCOUNT_ID_SEQ);
    }
}
