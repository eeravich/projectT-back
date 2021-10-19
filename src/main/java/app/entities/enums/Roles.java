package app.entities.enums;

import io.swagger.models.auth.In;

import java.util.HashMap;
import java.util.Map;

public enum Roles {
    ROLE_USER(1, Fields.ROLE_USER),
    ROLE_MANAGER(2, Fields.ROLE_MANAGER),
    ROLE_ADMIN(3, Fields.ROLE_ADMIN);

    private final Integer id;
    private final String name;

    private static final Map<Integer, Roles> rolesMap = new HashMap<>();
    static {
        for (Roles role : values()) {
            rolesMap.put(role.getId(), role);
        }
    }

    public static Roles getById(Integer id) {
        return rolesMap.get(id);
    }

    Roles(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static class Fields {
        public static final String ROLE_USER = "ROLE_USER";
        public static final String ROLE_MANAGER = "ROLE_MANAGER";
        public static final String ROLE_ADMIN = "ROLE_ADMIN";
    }
}
