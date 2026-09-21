package ru.mirea.samsonova.cloudid.domain.repository;

import java.util.List;

import ru.mirea.samsonova.cloudid.domain.models.Sighting;

public interface AtlasRepository {
    boolean save(Sighting sighting);

    List<Sighting> getAll();
}
