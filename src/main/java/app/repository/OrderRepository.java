package app.repository;

import app.entities.pojos.OrderPojo;
import app.generated.jooq.Sequences;
import app.generated.jooq.tables.pojos.Order;
import app.generated.jooq.tables.pojos.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static app.generated.jooq.Tables.ORDER;
import static app.generated.jooq.Tables.ORDER_STATUS;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final DSLContext dslContext;

    public List<OrderPojo> getList() {
        return dslContext.select(ORDER.fields())
                .select(ORDER_STATUS.NAME.as("statusName"))
                .from(ORDER)
                .leftJoin(ORDER_STATUS).on(ORDER.STATUS_ID.eq(ORDER_STATUS.ID))
                .where(ORDER.DELETED_DATETIME.isNull())
                .fetchInto(OrderPojo.class);
    }

    public OrderPojo getById(Long orderId) {
        return dslContext.select(ORDER.fields())
                .select(ORDER_STATUS.NAME.as("statusName"))
                .from(ORDER)
                .leftJoin(ORDER_STATUS).on(ORDER.STATUS_ID.eq(ORDER_STATUS.ID))
                .where(ORDER.ORDER_ID.eq(orderId), ORDER.DELETED_DATETIME.isNull())
                .fetchOneInto(OrderPojo.class);
    }

    public void createOrder(Order order) {
        dslContext.insertInto(ORDER)
                .set(ORDER.ORDER_ID, order.getOrderId())
                .set(ORDER.CREATED_DATETIME, LocalDateTime.now())
                .set(ORDER.ACCOUNT_ID, order.getAccountId())
                .set(ORDER.PHONE, order.getPhone())
                .set(ORDER.NAME, order.getName())
                .set(ORDER.IS_CASH, order.getIsCash())
                .set(ORDER.CHANGE, order.getChange())
                .set(ORDER.COMMENT, order.getComment())
                .set(ORDER.ADDONS_COUNT, order.getAddonsCount())
                .set(ORDER.DISCOUNT_ID, order.getDiscountId())
                .set(ORDER.DELIVERY_TIME, order.getDeliveryTime())
                .set(ORDER.EMAIL, order.getEmail())
                .set(ORDER.STATUS_ID, order.getStatusId())
                .set(ORDER.ADDRESS_ID, order.getAddressId())
                .execute();
    }

    public void deleteOrder(Long orderId) {
        dslContext.update(ORDER)
                .set(ORDER.DELETED_DATETIME, LocalDateTime.now())
                .where(ORDER.ORDER_ID.eq(orderId), ORDER.DELETED_DATETIME.isNull())
                .execute();
    }

    public Long getNextOrderId() {
        return dslContext.nextval(Sequences.ORDER_ORDER_ID_SEQ);
    }

    public List<OrderStatus> getOrderStatuses() {
        return dslContext.selectFrom(ORDER_STATUS)
                .fetchInto(OrderStatus.class);
    }
}
