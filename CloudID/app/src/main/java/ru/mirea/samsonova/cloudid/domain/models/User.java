package ru.mirea.samsonova.cloudid.domain.models;

public class User {
    private int id;
    private String login;
    private boolean guest;

    public User(int id, String login, boolean guest) {
        this.id = id;
        this.login = login;
        this.guest = guest;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public boolean isGuest() {
        return guest;
    }
}
