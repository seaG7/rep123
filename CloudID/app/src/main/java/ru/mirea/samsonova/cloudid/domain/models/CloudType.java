package ru.mirea.samsonova.cloudid.domain.models;

public class CloudType {
    private String code;
    private String name;
    private String latin;
    private String imageUrl;
    private String extract;

    public CloudType(String code, String name, String latin, String imageUrl, String extract) {
        this.code = code;
        this.name = name;
        this.latin = latin;
        this.imageUrl = imageUrl;
        this.extract = extract;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getLatin() {
        return latin;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getExtract() {
        return extract;
    }
}
