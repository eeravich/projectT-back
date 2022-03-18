package app.repository.product;

import app.generated.projectT.tables.pojos.Product;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static app.generated.projectT.Tables.PRODUCT;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductListRepository {
    private final DSLContext dslContext;

    public List<Product> getList() {
        return dslContext.selectFrom(PRODUCT)
                .fetchInto(Product.class);
    }
}
