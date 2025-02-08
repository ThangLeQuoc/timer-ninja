package io.github.thanglequoc.timerninja.servicesample;

public class User {

    private int id;
    private String userName;

    public User(int id, String userName) {
        this.id = id;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Id: '" + this.id + "', Username: '" + this.userName + "'";
    }
}
