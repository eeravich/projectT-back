package app.repository;

import app.entities.pojos.ProductPojo;
import app.generated.jooq.Sequences;
import app.generated.jooq.tables.pojos.Component;
import app.generated.jooq.tables.pojos.Discount;
import app.generated.jooq.tables.pojos.Product;
import app.generated.jooq.tables.pojos.RefProductComponent;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static app.generated.jooq.Tables.*;

@Repository
@RequiredArgsConstructor
public class ProductRepository {
    private final DSLContext dslContext;

    public List<ProductPojo> getList() {
        return dslContext.select(PRODUCT.fields())
                .select(PRODUCT_TYPE.NAME.as("productTypeName"))
                .from(PRODUCT)
                .leftJoin(PRODUCT_TYPE).on(PRODUCT.PRODUCT_TYPE_ID.eq(PRODUCT_TYPE.ID))
                .where(PRODUCT.DELETED_DATETIME.isNull())
                .fetchInto(ProductPojo.class);
    }

    public ProductPojo getById(Long productId) {
        return dslContext.select(PRODUCT.fields())
                .select(PRODUCT_TYPE.NAME.as("productTypeName"))
                .from(PRODUCT)
                .leftJoin(PRODUCT_TYPE).on(PRODUCT.PRODUCT_TYPE_ID.eq(PRODUCT_TYPE.ID))
                .where(PRODUCT.PRODUCT_ID.eq(productId), PRODUCT.DELETED_DATETIME.isNull())
                .fetchOneInto(ProductPojo.class);
    }

    public void createProduct(Product product) {
        dslContext.insertInto(PRODUCT)
                .set(PRODUCT.CREATED_DATETIME, LocalDateTime.now())
                .set(PRODUCT.PRODUCT_ID, product.getProductId())
                .set(PRODUCT.NAME, product.getName())
                .set(PRODUCT.DESCRIPTION, product.getDescription())
                .set(PRODUCT.PRICE, product.getPrice())
                .set(PRODUCT.PORTION, product.getPortion())
                .set(PRODUCT.WEIGHT, product.getWeight())
                .set(PRODUCT.PRODUCT_TYPE_ID, product.getProductTypeId())
                .set(PRODUCT.ATTACHMENT_ID, product.getAttachmentId())
                .execute();
    }

    public void deleteProduct(Long productId) {
        dslContext.update(PRODUCT)
                .set(PRODUCT.DELETED_DATETIME, LocalDateTime.now())
                .where(PRODUCT.PRODUCT_ID.eq(productId), PRODUCT.DELETED_DATETIME.isNull())
                .execute();
    }

    public Long getNextProductId() {
        return dslContext.nextval(Sequences.PRODUCT_PRODUCT_ID_SEQ);
    }

    public List<Component> getProductComponents(Long productId) {
        return dslContext.select(COMPONENT.fields())
                .from(REF_PRODUCT_COMPONENT)
                .leftJoin(COMPONENT).on(COMPONENT.COMPONENT_ID.eq(REF_PRODUCT_COMPONENT.COMPONENT_ID))
                .where(REF_PRODUCT_COMPONENT.PRODUCT_ID.eq(productId), REF_PRODUCT_COMPONENT.IS_ACTUAL.isTrue())
                .fetchInto(Component.class);
    }

    public void addComponent(RefProductComponent refProductComponent) {
        dslContext.insertInto(REF_PRODUCT_COMPONENT)
                .set(REF_PRODUCT_COMPONENT.COMPONENT_ID, refProductComponent.getComponentId())
                .set(REF_PRODUCT_COMPONENT.PRODUCT_ID, refProductComponent.getProductId())
                .set(REF_PRODUCT_COMPONENT.IS_ACTUAL, true)
                .execute();
    }

    public void deleteComponent(Long productId, Long componentId) {
        dslContext.update(REF_PRODUCT_COMPONENT)
                .set(REF_PRODUCT_COMPONENT.IS_ACTUAL, false)
                .where(REF_PRODUCT_COMPONENT.PRODUCT_ID.eq(productId), REF_PRODUCT_COMPONENT.COMPONENT_ID.eq(componentId))
                .execute();
    }

    public List<Discount> getDiscountsByProductId(Long productId) {
        return dslContext.select(DISCOUNT.fields())
                .from(REF_PRODUCT_DISCOUNT)
                .leftJoin(DISCOUNT).on(REF_PRODUCT_DISCOUNT.DISCOUNT_ID.eq(DISCOUNT.DISCOUNT_ID))
                .where(REF_PRODUCT_DISCOUNT.PRODUCT_ID.eq(productId), DISCOUNT.DELETED_DATETIME.isNull())
                .fetchInto(Discount.class);
    }
}
