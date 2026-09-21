package ru.mirea.samsonova.cloudid.domain.models;

public class Classification {
    private String code;
    private String name;
    private float confidence;

    public Classification(String code, String name, float confidence) {
        this.code = code;
        this.name = name;
        this.confidence = confidence;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public float getConfidence() {
        return confidence;
    }
}
