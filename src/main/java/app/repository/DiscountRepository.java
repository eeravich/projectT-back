package app.repository;

import app.generated.jooq.Sequences;
import app.generated.jooq.tables.pojos.Discount;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static app.generated.jooq.Tables.DISCOUNT;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DiscountRepository {
    private final DSLContext dslContext;

    public List<Discount> getList() {
        return dslContext.selectFrom(DISCOUNT)
                .where(DISCOUNT.DELETED_DATETIME.isNull())
                .fetchInto(Discount.class);
    }

    public Discount getById(Long discountId) {
        return dslContext.selectFrom(DISCOUNT)
                .where(DISCOUNT.DISCOUNT_ID.eq(discountId), DISCOUNT.DELETED_DATETIME.isNull())
                .fetchOneInto(Discount.class);
    }

    public void createDiscount(Discount discount) {
        dslContext.insertInto(DISCOUNT)
                .set(DISCOUNT.DISCOUNT_ID, discount.getDiscountId())
                .set(DISCOUNT.CREATED_DATETIME, LocalDateTime.now())
                .set(DISCOUNT.NAME, discount.getName())
                .set(DISCOUNT.DESCRIPTION, discount.getDescription())
                .set(DISCOUNT.PERCENT, discount.getPercent())
                .set(DISCOUNT.PROMOCODE, discount.getPromocode())
                .set(DISCOUNT.IS_ACTIVE, discount.getIsActive())
                .set(DISCOUNT.START_DATETIME, discount.getStartDatetime())
                .set(DISCOUNT.END_DATETIME, discount.getEndDatetime())
                .execute();
    }

    public void deleteDiscount(Long discountId) {
        dslContext.update(DISCOUNT)
                .set(DISCOUNT.DELETED_DATETIME, LocalDateTime.now())
                .where(DISCOUNT.DISCOUNT_ID.eq(discountId), DISCOUNT.DELETED_DATETIME.isNull())
                .execute();
    }

    public Long getNextDiscountId() {
        return dslContext.nextval(Sequences.DISCOUNT_DISCOUNT_ID_SEQ);
    }
}
