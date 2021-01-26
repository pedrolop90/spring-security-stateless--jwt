package com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * @author pedro
 */
@Data
@AllArgsConstructor
public class UserEntity {

    private String username;
    private String password;
    private List<GrantedAuthority> authorities;

}
