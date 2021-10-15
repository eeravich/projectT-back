package app.config;


import app.UserContextFilter;
import app.services.DefaultUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConf extends WebSecurityConfigurerAdapter {
    private final DefaultUserDetailService defaultUserDetailService;
    private final UserContextFilter userContextFilter;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(defaultUserDetailService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/auth", "/register").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .and()
                .sessionManagement()
                .and()
                .logout(logout -> logout.logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .addLogoutHandler(((request, response, authentication) -> {
                            try {
                                request.logout();
                            } catch (ServletException e) {
                                e.printStackTrace();
                            }
                        }))
                );
        http.addFilterAfter(userContextFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
