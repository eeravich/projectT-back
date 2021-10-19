package app.entities.enums;

import java.util.HashMap;
import java.util.Map;

public enum ProductTypes {
    PIZZA(1),
    WOK(2),
    ROLLS(3),
    SUSHI(4),
    SALAD(5),
    SOUP(6),
    DRINKS(7);

    private final Integer id;
    private static final Map<Integer, ProductTypes> productTypeMap = new HashMap<>();
    static {
        for (ProductTypes productTypes : values()) {
            productTypeMap.put(productTypes.getId(), productTypes);
        }
    }

    public static ProductTypes getById(Integer id) {
        return productTypeMap.get(id);
    }

    ProductTypes(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
