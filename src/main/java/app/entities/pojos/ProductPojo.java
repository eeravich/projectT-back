package app.entities.pojos;

import app.generated.jooq.tables.pojos.Component;
import app.generated.jooq.tables.pojos.Discount;
import app.generated.jooq.tables.pojos.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductPojo extends Product {
    private String productTypeName;
    private List<Component> components;
    private List<Discount> discounts;

    public ProductPojo(Product product) {
        super(product);
    }
}
