package ru.mirea.samsonova.cloudid.domain.models;

public class User {
    private int id;
    private String login;
    private boolean guest;
    private String localDate;

    public User(int id, String login, boolean guest) {
        this(id, login, guest, "");
    }

    public User(int id, String login, boolean guest, String localDate) {
        this.id = id;
        this.login = login;
        this.guest = guest;
        this.localDate = localDate;
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

    public String getLocalDate() {
        return localDate;
    }
}
