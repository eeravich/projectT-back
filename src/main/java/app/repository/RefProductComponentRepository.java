package app.repository;

import app.generated.projectT.tables.daos.RefProductComponentDao;
import app.generated.projectT.tables.pojos.RefProductComponent;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static app.generated.projectT.Tables.REF_PRODUCT_COMPONENT;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RefProductComponentRepository {
    private final DSLContext dslContext;
    private final RefProductComponentDao refProductComponentDao;

    public void deleteRefsByProductId(Long productId) {
        dslContext.update(REF_PRODUCT_COMPONENT)
                .set(REF_PRODUCT_COMPONENT.IS_ACTUAL, false)
                .where(REF_PRODUCT_COMPONENT.PRODUCT_ID.eq(productId))
                .execute();
    }

    public void deleteRefsByComponentId(Long componentId) {
        dslContext.update(REF_PRODUCT_COMPONENT)
                .set(REF_PRODUCT_COMPONENT.IS_ACTUAL, false)
                .where(REF_PRODUCT_COMPONENT.COMPONENT_ID.eq(componentId))
                .execute();
    }

    public List<RefProductComponent> getActualRefsByProductId(Long productId) {
        return dslContext.selectFrom(REF_PRODUCT_COMPONENT)
                .where(
                        REF_PRODUCT_COMPONENT.PRODUCT_ID.eq(productId),
                        REF_PRODUCT_COMPONENT.IS_ACTUAL.isTrue()
                )
                .fetchInto(RefProductComponent.class);
    }

    public void createNewRefs(List<RefProductComponent> refs) {
        refProductComponentDao.insert(refs);
    }
}
