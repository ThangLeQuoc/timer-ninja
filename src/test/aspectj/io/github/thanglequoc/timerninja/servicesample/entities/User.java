package io.github.thanglequoc.timerninja.servicesample.entities;

public class User {

    private int id;
    private String userName;
    private String email;

    public User(int id, String userName, String email) {
        this.id = id;
        this.userName = userName;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
