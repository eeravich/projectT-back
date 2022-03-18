package app.repository;

import app.generated.projectT.tables.daos.RefProductDiscountDao;
import app.generated.projectT.tables.pojos.RefProductDiscount;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static app.generated.projectT.Tables.REF_PRODUCT_DISCOUNT;

@Repository
@RequiredArgsConstructor
public class RefProductDiscountRepository {
    private final DSLContext dslContext;
    private final RefProductDiscountDao refProductDiscountDao;

    public void deleteRefByProductId(Long productId) {
        dslContext.update(REF_PRODUCT_DISCOUNT)
                .set(REF_PRODUCT_DISCOUNT.IS_ACTUAL, false)
                .where(REF_PRODUCT_DISCOUNT.PRODUCT_ID.eq(productId))
                .execute();
    }

    public void deleteRefByDiscountId(Long discountId) {
        dslContext.update(REF_PRODUCT_DISCOUNT)
                .set(REF_PRODUCT_DISCOUNT.IS_ACTUAL, false)
                .where(REF_PRODUCT_DISCOUNT.DISCOUNT_ID.eq(discountId))
                .execute();
    }

    public List<RefProductDiscount> getActualRefsByProductId(Long productId) {
        return dslContext.selectFrom(REF_PRODUCT_DISCOUNT)
                .where(
                        REF_PRODUCT_DISCOUNT.PRODUCT_ID.eq(productId),
                        REF_PRODUCT_DISCOUNT.IS_ACTUAL.isTrue()
                )
                .fetchInto(RefProductDiscount.class);
    }

    public void createNewRefs(List<RefProductDiscount> refs) {
        refProductDiscountDao.insert(refs);
    }
}
