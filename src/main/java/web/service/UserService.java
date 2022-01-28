package web.service;

import web.model.User;

import java.util.List;

public interface UserService {
    User findByUsername(String name);

    List<User> listUsers();

    void registerUser(User user);

    void deleteUser(User user);

    void editUser(User user);

    User getUserById(long id);
}
