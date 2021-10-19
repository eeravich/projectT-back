package app.entities.enums;

import java.util.HashMap;
import java.util.Map;

public enum OrderStatuses {
    NEW(1),
    SUBMITTED(2),
    IN_PROGRESS(3),
    READY(4),
    DELIVERY(5),
    FINISHED(6),
    CANCELED(7);

    private final Integer id;
    private static final Map<Integer, OrderStatuses> orderStatusMap = new HashMap<>();
    static {
        for (OrderStatuses orderStatuses : values()) {
            orderStatusMap.put(orderStatuses.getId(), orderStatuses);
        }
    }

    public static OrderStatuses getById(Integer id) {
        return orderStatusMap.get(id);
    }

    OrderStatuses(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
