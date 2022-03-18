package app.repository;

import app.Utils;
import app.generated.projectT.Sequences;
import app.generated.projectT.tables.daos.ProductDao;
import app.generated.projectT.tables.pojos.Product;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static app.generated.projectT.Tables.PRODUCT;

@Repository
@RequiredArgsConstructor
public class ProductRepository {
    private final DSLContext dslContext;
    private final ProductDao productDao;

    public List<Product> getList() {
        return dslContext.selectFrom(PRODUCT)
                .where(PRODUCT.DELETED_DATETIME.isNull())
                .fetchInto(Product.class);
    }

    public Optional<Product> getActualProduct(Long productEntityId) {
        return dslContext.selectFrom(PRODUCT)
                .where(
                        PRODUCT.ENTITY_ID.eq(productEntityId),
                        PRODUCT.DELETED_DATETIME.isNull()
                )
                .fetchOptionalInto(Product.class);
    }

    public void createProduct(Product product) {
        productDao.insert(product);
    }

    public void deleteProduct(Long productId) {
        dslContext.update(PRODUCT)
                .set(PRODUCT.DELETED_DATETIME, Utils.now())
                .where(PRODUCT.ID.eq(productId))
                .execute();
    }

    public Long getNextProductId() {
        return dslContext.nextval(Sequences.PRODUCT_ID_SEQ);
    }

//    public List<Component> getProductComponents(Long productId) {
//        return dslContext.select(COMPONENT.fields())
//                .from(REF_PRODUCT_COMPONENT)
//                .leftJoin(COMPONENT).on(COMPONENT.COMPONENT_ID.eq(REF_PRODUCT_COMPONENT.COMPONENT_ID))
//                .where(REF_PRODUCT_COMPONENT.PRODUCT_ID.eq(productId), REF_PRODUCT_COMPONENT.IS_ACTUAL.isTrue())
//                .fetchInto(Component.class);
//    }
//
//    public void addComponent(RefProductComponent refProductComponent) {
//        dslContext.insertInto(REF_PRODUCT_COMPONENT)
//                .set(REF_PRODUCT_COMPONENT.COMPONENT_ID, refProductComponent.getComponentId())
//                .set(REF_PRODUCT_COMPONENT.PRODUCT_ID, refProductComponent.getProductId())
//                .set(REF_PRODUCT_COMPONENT.IS_ACTUAL, true)
//                .execute();
//    }
//
//    public void deleteComponent(Long productId, Long componentId) {
//        dslContext.update(REF_PRODUCT_COMPONENT)
//                .set(REF_PRODUCT_COMPONENT.IS_ACTUAL, false)
//                .where(REF_PRODUCT_COMPONENT.PRODUCT_ID.eq(productId), REF_PRODUCT_COMPONENT.COMPONENT_ID.eq(componentId))
//                .execute();
//    }
//
//    public List<Discount> getDiscountsByProductId(Long productId) {
//        return dslContext.select(DISCOUNT.fields())
//                .from(REF_PRODUCT_DISCOUNT)
//                .leftJoin(DISCOUNT).on(REF_PRODUCT_DISCOUNT.DISCOUNT_ID.eq(DISCOUNT.DISCOUNT_ID))
//                .where(REF_PRODUCT_DISCOUNT.PRODUCT_ID.eq(productId), DISCOUNT.DELETED_DATETIME.isNull())
//                .fetchInto(Discount.class);
//    }
//
//    public List<ProductType> getProductTypes() {
//        return dslContext.selectFrom(PRODUCT_TYPE)
//                .fetchInto(ProductType.class);
//    }
//
//    public List<Product> getListByProductIds(List<Long> productIdList) {
//        return dslContext.select(PRODUCT.fields())
//                .from(PRODUCT)
//                .where(PRODUCT.PRODUCT_ID.in(productIdList), PRODUCT.DELETED_DATETIME.isNull())
//                .fetchInto(Product.class);
//    }
}
