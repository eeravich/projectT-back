package app.repository;

import app.generated.projectT.tables.daos.RefOrderProductDao;
import app.generated.projectT.tables.pojos.RefOrderProduct;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static app.generated.projectT.Tables.REF_ORDER_PRODUCT;

@Repository
@RequiredArgsConstructor
public class RefOrderProductRepository {
    private final DSLContext dslContext;
    private final RefOrderProductDao refOrderProductDao;

    public void deleteRefByProductId(Long productId) {
        dslContext.update(REF_ORDER_PRODUCT)
                .set(REF_ORDER_PRODUCT.IS_ACTUAL, false)
                .where(REF_ORDER_PRODUCT.PRODUCT_ID.eq(productId))
                .execute();
    }

    public void deleteRefByOrderId(Long orderId) {
        dslContext.update(REF_ORDER_PRODUCT)
                .set(REF_ORDER_PRODUCT.IS_ACTUAL, false)
                .where(REF_ORDER_PRODUCT.ORDER_ID.eq(orderId))
                .execute();
    }

    public List<RefOrderProduct> getActualRefsByProductId(Long productId) {
        return dslContext.selectFrom(REF_ORDER_PRODUCT)
                .where(
                        REF_ORDER_PRODUCT.PRODUCT_ID.eq(productId),
                        REF_ORDER_PRODUCT.IS_ACTUAL.isTrue()
                )
                .fetchInto(RefOrderProduct.class);
    }

    public void createNewRefs(List<RefOrderProduct> refs) {
        refOrderProductDao.insert(refs);
    }
}
