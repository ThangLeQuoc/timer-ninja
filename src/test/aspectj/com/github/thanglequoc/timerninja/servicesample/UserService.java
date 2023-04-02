package com.github.thanglequoc.timerninja.servicesample;

public class UserService {

    public User findUser(int userId) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        User user = new User(userId, "user-"+userId);
        return user;
    }
}
