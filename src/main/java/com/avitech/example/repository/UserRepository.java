package com.avitech.example.repository;

import com.avitech.example.model.User;

import java.util.List;

public interface UserRepository {

    User save(User user);

    List<User> findAll();

    void deleteAll();
}
