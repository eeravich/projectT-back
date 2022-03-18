package app;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
@Getter
@Setter
public class UserContext {
    private Long accountId;
    private String login;
    private String sessionId;
    private String email;
    private String name;
    private String surname;
    private String phone;
    private Integer roleId;
}
