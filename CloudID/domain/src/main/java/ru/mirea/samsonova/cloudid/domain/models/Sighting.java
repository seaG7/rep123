package ru.mirea.samsonova.cloudid.domain.models;

public class Sighting {
    private int id;
    private String cloudCode;
    private String cloudName;
    private String note;
    private String takenAt;
    private String photoUri;

    public Sighting(int id, String cloudCode, String cloudName, String note) {
        this(id, cloudCode, cloudName, note, "", "");
    }

    public Sighting(int id, String cloudCode, String cloudName, String note, String takenAt, String photoUri) {
        this.id = id;
        this.cloudCode = cloudCode;
        this.cloudName = cloudName;
        this.note = note;
        this.takenAt = takenAt;
        this.photoUri = photoUri;
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

    public String getTakenAt() {
        return takenAt;
    }

    public String getPhotoUri() {
        return photoUri;
    }
}
