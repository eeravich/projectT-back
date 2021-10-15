package app;

import org.jooq.tools.json.JSONObject;

import java.util.Collections;
import java.util.Map;

public class InvalidDataException extends RuntimeException {
    private final Map<String, String> errors;

    public InvalidDataException(Map<String, String> errors) {
        super("error");
        this.errors = errors;
    }

    public InvalidDataException(String message) {
        super(message);
        this.errors = Collections.singletonMap("error", message);
    }

    public InvalidDataException(String key, String message) {
        super("error");
        this.errors = Collections.singletonMap(key, message);
    }

    @Override
    public String getMessage() {
        return new JSONObject(errors).toString();
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
