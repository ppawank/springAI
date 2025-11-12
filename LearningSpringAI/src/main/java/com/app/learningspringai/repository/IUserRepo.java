package com.app.learningspringai.repository;


import org.springframework.security.core.userdetails.User;

public interface IUserRepo {

    public User findByUsername(String username);
}
