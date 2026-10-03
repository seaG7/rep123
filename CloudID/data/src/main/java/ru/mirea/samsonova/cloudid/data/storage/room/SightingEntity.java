package ru.mirea.samsonova.cloudid.data.storage.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "sightings")
public class SightingEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @NonNull
    public String cloudCode = "";
    @NonNull
    public String cloudName = "";
    @NonNull
    public String note = "";
    @NonNull
    public String takenAt = "";
    @NonNull
    public String photoUri = "";

    public SightingEntity() {
    }

    @Ignore
    public SightingEntity(int id, String cloudCode, String cloudName, String note, String takenAt, String photoUri) {
        this.id = id;
        this.cloudCode = cloudCode;
        this.cloudName = cloudName;
        this.note = note == null ? "" : note;
        this.takenAt = takenAt == null ? "" : takenAt;
        this.photoUri = photoUri == null ? "" : photoUri;
    }
}
