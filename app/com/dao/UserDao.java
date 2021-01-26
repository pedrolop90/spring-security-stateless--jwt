package com.dao;

import com.entity.UserEntity;

import java.util.Optional;

/**
 * @author pedro
 */
public interface UserDao {

    Optional<UserEntity> findByUsername(String username);

}
