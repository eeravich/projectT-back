package app.entities.pojos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ErrorPojo {
    private String message;
    private Map<String, String> errors;
    private Integer status;
}
