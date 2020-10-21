package com.siaivo.shipments.service;

import com.siaivo.shipments.model.User;

import java.util.List;

public interface UserService {
    User findUserByEmail(String email);
    User findUserById(int id);
    void saveUser(User user);
    void editUserType(String userType, int userId);
    void editUserPassword(String password, int userId);
    void editUserName(String name, int userId);
    List<User> allUsers();
}