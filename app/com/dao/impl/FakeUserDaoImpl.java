package com.dao.impl;

import com.dao.UserDao;
import com.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author pedro
 */
@Repository("fake")
@RequiredArgsConstructor
public class FakeUserDaoImpl implements UserDao {

    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return llenar()
                .stream()
                .filter(userEntity -> userEntity.getUsername().equals(username))
                .findFirst();
    }

    private List<UserEntity> llenar() {
        List<UserEntity> userEntities = Arrays.asList(
                new UserEntity(
                        "pedro",
                        passwordEncoder.encode("123456"),
                        Arrays.asList(
                                new SimpleGrantedAuthority("ROLE_PERSONA")
                        )
                ),
                new UserEntity(
                        "andres",
                        passwordEncoder.encode("123456"),
                        Arrays.asList(
                                new SimpleGrantedAuthority("ROLE_PERSONA"),
                                new SimpleGrantedAuthority("ROLE_ESTUDIANTE")
                        )
                ),
                new UserEntity(
                        "julian",
                        passwordEncoder.encode("123456"),
                        Arrays.asList(
                                new SimpleGrantedAuthority("ROLE_ESTUDIANTE")
                        )
                )
        );
        return userEntities;
    }

}
