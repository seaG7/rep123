package ru.mirea.samsonova.cloudid.domain.models;

import java.util.List;

public class CatalogLoad {
    private final List<CloudType> types;
    private final String warning;

    public CatalogLoad(List<CloudType> types, String warning) {
        this.types = types;
        this.warning = warning == null ? "" : warning;
    }

    public List<CloudType> getTypes() {
        return types;
    }

    public String getWarning() {
        return warning;
    }
}
