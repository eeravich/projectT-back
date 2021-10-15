package app.entities.pojos;

import app.generated.jooq.tables.pojos.Attachment;
import app.generated.jooq.tables.pojos.Combo;
import app.generated.jooq.tables.pojos.Discount;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ComboPojo extends Combo {
    private List<ProductPojo> products;
    private Attachment attachment;
    private List<Discount> discounts;
}
