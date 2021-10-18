package app.entities.pojos;

import app.generated.jooq.tables.pojos.Account;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountPojo extends Account {
    private String roleName;
}
