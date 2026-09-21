package ru.mirea.samsonova.cloudid.domain.models;

public class Sighting {
    private int id;
    private String cloudCode;
    private String cloudName;
    private String note;

    public Sighting(int id, String cloudCode, String cloudName, String note) {
        this.id = id;
        this.cloudCode = cloudCode;
        this.cloudName = cloudName;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public String getCloudCode() {
        return cloudCode;
    }

    public String getCloudName() {
        return cloudName;
    }

    public String getNote() {
        return note;
    }
}
