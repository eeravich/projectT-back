package app.services;

import app.entities.pojos.AccountPojo;
import app.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultUserDetailService implements UserDetailsService {
    final private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        AccountPojo account = accountRepository.findByLogin(login);
        if (account == null) {
            throw new UsernameNotFoundException("User" + login + " not found");
        }
        List<GrantedAuthority> authList = Collections.singletonList(new SimpleGrantedAuthority(account.getRoleName()));
        return new User(account.getLogin(), account.getPassword(), authList);
    }
}
