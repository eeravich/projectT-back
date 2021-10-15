package app.repository;

import app.generated.jooq.tables.pojos.OrderStatus;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static app.generated.jooq.Tables.ORDER_STATUS;

@Repository
public class TestRepo {

    final DSLContext dslContext;

    public TestRepo(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public List<OrderStatus> GetStatuses() {
        return dslContext.selectFrom(ORDER_STATUS)
                .fetchInto(OrderStatus.class);
    }
}
