package app.entities.pojos;

import app.generated.jooq.tables.pojos.Account;
import app.generated.jooq.tables.pojos.Address;
import app.generated.jooq.tables.pojos.Discount;
import app.generated.jooq.tables.pojos.Order;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderPojo extends Order {
    private String statusName;
    private Address address;
    private Account account;
    private Discount discount;
}
