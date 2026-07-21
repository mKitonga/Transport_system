package com.transport.service;

import com.transport.entity.User;

import java.util.List;

/**
 * Service interface defining business operations for {@link User} management.
 */
public interface UserService {

    User registerUser(User user);

    User getUserById(Long id);

    User getUserByEmail(String email);

    List<User> getAllUsers();

    User updateUser(Long id, User user);

    void deleteUser(Long id);
}
