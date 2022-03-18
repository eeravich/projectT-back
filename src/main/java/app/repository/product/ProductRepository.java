package app.repository.product;

import app.Utils;
import app.generated.projectT.Sequences;
import app.generated.projectT.tables.daos.ProductDao;
import app.generated.projectT.tables.pojos.Product;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static app.generated.projectT.Tables.PRODUCT;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepository {
    private final DSLContext dslContext;
    private final ProductDao productDao;

    public Long getNextId() {
        return dslContext.nextval(Sequences.PRODUCT_ID_SEQ);
    }

    public Optional<Product> getProduct(Long productId) {
        return dslContext.selectFrom(PRODUCT)
                .where(PRODUCT.ID.eq(productId))
                .fetchOptionalInto(Product.class);
    }

    public void deleteProduct(Long productId) {
        dslContext.update(PRODUCT)
                .set(PRODUCT.DELETED_DATETIME, Utils.now())
                .where(PRODUCT.ID.eq(productId))
                .execute();
    }

    public void createProduct(Product product) {
        productDao.insert(product);
    }


}
