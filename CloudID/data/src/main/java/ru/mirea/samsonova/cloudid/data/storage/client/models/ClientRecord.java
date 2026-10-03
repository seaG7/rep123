package ru.mirea.samsonova.cloudid.data.storage.client.models;

public class ClientRecord {
    private final int id;
    private final String email;
    private final boolean guest;
    private final String localDate;

    public ClientRecord(int id, String email, boolean guest, String localDate) {
        this.id = id;
        this.email = email;
        this.guest = guest;
        this.localDate = localDate;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public boolean isGuest() {
        return guest;
    }

    public String getLocalDate() {
        return localDate;
    }
}
