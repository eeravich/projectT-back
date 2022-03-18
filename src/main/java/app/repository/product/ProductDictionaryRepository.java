package app.repository.product;

import app.generated.projectT.tables.pojos.ProductType;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static app.generated.projectT.Tables.PRODUCT_TYPE;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductDictionaryRepository {
    private final DSLContext dslContext;

    public List<ProductType> getProductTypeList() {
        return dslContext.selectFrom(PRODUCT_TYPE)
                .fetchInto(ProductType.class);
    }

}
