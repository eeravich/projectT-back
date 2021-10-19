package app;

import app.entities.pojos.AccountPojo;
import app.generated.jooq.tables.pojos.Account;
import app.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class UserContextFilter extends OncePerRequestFilter {
    private final UserContext userContext;
    private final AccountRepository accountRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = null;

        if (authentication != null) {
            userDetails = (UserDetails) authentication.getPrincipal();
        }

        if (userDetails != null && userDetails.getUsername() != null) {
            Account account = accountRepository.findByLogin(userDetails.getUsername());
            userContext.setSessionId(request.getRequestedSessionId());
            userContext.setLogin(account.getLogin());
            userContext.setEmail(account.getEmail());
            userContext.setName(account.getName());
            userContext.setSurname(account.getSurname());
            userContext.setPhone(account.getPhone());
            userContext.setRoleId(account.getRoleId());
        }

        filterChain.doFilter(request, response);
    }


//    private void setUpSpringSecurityContext(String login, HttpServletRequest request) {
//        UserDetails userDetails = new User(login, "", new ArrayList<>());
//
//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//                userDetails, null, userDetails.getAuthorities());
//        usernamePasswordAuthenticationToken
//                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//    }
}
