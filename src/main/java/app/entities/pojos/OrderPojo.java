package app.entities.pojos;

import app.generated.jooq.tables.pojos.Account;
import app.generated.jooq.tables.pojos.Address;
import app.generated.jooq.tables.pojos.Discount;
import app.generated.jooq.tables.pojos.Order;
import app.generated.jooq.tables.pojos.Product;
import app.generated.jooq.tables.pojos.Combo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderPojo extends Order {
    private String statusName;
    private Address address;
    private Account account;
    private Discount discount;
    private List<Product> productList;
    private List<Combo> comboList;

    public OrderPojo(Order order) {
        super(order);
    }
}
