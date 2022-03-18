package app.repository;

import app.Utils;
import app.generated.projectT.Sequences;
import app.generated.projectT.tables.daos.OrderDao;
import app.generated.projectT.tables.pojos.Order;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static app.generated.projectT.Tables.ORDER;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final DSLContext dslContext;
    private final OrderDao orderDao;

    public List<Order> getList() {
        return dslContext.selectFrom(ORDER)
                .where(ORDER.DELETED_DATETIME.isNull())
                .fetchInto(Order.class);
    }

    public Optional<Order> getActualOrder(Long orderEntityId) {
        return dslContext.selectFrom(ORDER)
                .where(
                        ORDER.ENTITY_ID.eq(orderEntityId),
                        ORDER.DELETED_DATETIME.isNull()
                )
                .fetchOptionalInto(Order.class);
    }

    public void createOrder(Order order) {
        orderDao.insert(order);
    }

    public void deleteOrder(Long orderId) {
        dslContext.update(ORDER)
                .set(ORDER.DELETED_DATETIME, Utils.now())
                .where(ORDER.ID.eq(orderId))
                .execute();
    }

    public Long getNextOrderId() {
        return dslContext.nextval(Sequences.ORDER_ID_SEQ);
    }

//    public List<OrderStatus> getOrderStatuses() {
//        return dslContext.selectFrom(ORDER_STATUS)
//                .fetchInto(OrderStatus.class);
//    }
//
//    public List<Long> getProductIdsByOrderId(Long orderId) {
//        return dslContext.select(REF_ORDER_PRODUCT.PRODUCT_ID)
//                .from(ORDER)
//                .join(REF_ORDER_PRODUCT).on(ORDER.ORDER_ID.eq(REF_ORDER_PRODUCT.ORDER_ID))
//                .where(ORDER.ORDER_ID.eq(orderId), ORDER.DELETED_DATETIME.isNull())
//                .fetchInto(Long.class);
//    }
//
//    public List<Long> getComboIdsByOrderId(Long orderId) {
//        return dslContext.select(REF_ORDER_PRODUCT.COMBO_ID)
//                .from(ORDER)
//                .join(REF_ORDER_PRODUCT).on(ORDER.ORDER_ID.eq(REF_ORDER_PRODUCT.ORDER_ID))
//                .where(ORDER.ORDER_ID.eq(orderId), ORDER.DELETED_DATETIME.isNull())
//                .fetchInto(Long.class);
//    }
}
