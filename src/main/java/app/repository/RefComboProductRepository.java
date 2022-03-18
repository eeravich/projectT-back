package app.repository;

import app.generated.projectT.tables.daos.RefComboProductDao;
import app.generated.projectT.tables.pojos.RefComboProduct;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static app.generated.projectT.Tables.REF_COMBO_PRODUCT;

@Repository
@RequiredArgsConstructor
public class RefComboProductRepository {
    private final DSLContext dslContext;
    private final RefComboProductDao refComboProductDao;

    public void deleteRefByProductId(Long productId) {
        dslContext.update(REF_COMBO_PRODUCT)
                .set(REF_COMBO_PRODUCT.IS_ACTUAL, false)
                .where(REF_COMBO_PRODUCT.PRODUCT_ID.eq(productId))
                .execute();
    }

    public void deleteRefByComboId(Long comboId) {
        dslContext.update(REF_COMBO_PRODUCT)
                .set(REF_COMBO_PRODUCT.IS_ACTUAL, false)
                .where(REF_COMBO_PRODUCT.COMBO_ID.eq(comboId))
                .execute();
    }

    public List<RefComboProduct> getActualRefsByProductId(Long productId) {
        return dslContext.selectFrom(REF_COMBO_PRODUCT)
                .where(
                        REF_COMBO_PRODUCT.PRODUCT_ID.eq(productId),
                        REF_COMBO_PRODUCT.IS_ACTUAL.isTrue()
                )
                .fetchInto(RefComboProduct.class);
    }

    public void createNewRefs(List<RefComboProduct> refs) {
        refComboProductDao.insert(refs);
    }
}
