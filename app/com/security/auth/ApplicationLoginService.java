package com.security.auth;

import com.dao.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author pedro
 */
@Component
@RequiredArgsConstructor
public class ApplicationLoginService implements UserDetailsService {

    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao
                .findByUsername(username)
                .map(applicationUser ->
                        new User(
                                applicationUser.getUsername(),
                                applicationUser.getPassword(),
                                applicationUser.getAuthorities()
                        )
                )
                .get();
    }
}
