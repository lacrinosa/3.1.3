package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {
    public List<User> getAllUsers();
    public User getUserById(long id);
    public User findByEmail(String email);
    public void addUser(User user);
    public void updateUser(long id, User updatedUser);
    public void deleteUser(Long id);
    public User findByUsername(String email);
}
