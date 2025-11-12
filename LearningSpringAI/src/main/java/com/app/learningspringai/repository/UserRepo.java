package com.app.learningspringai.repository;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepo implements IUserRepo {
    @Override
    public User findByUsername(String username) {
        return (User) User.builder().username("admin").password("admin123").roles("USER", "ADMIN").build();
    }
}
