package ru.mirea.samsonova.cloudid.data.repository;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.samsonova.cloudid.domain.models.Sighting;
import ru.mirea.samsonova.cloudid.domain.repository.AtlasRepository;

public class AtlasRepositoryImpl implements AtlasRepository {
    private final List<Sighting> items;

    public AtlasRepositoryImpl(Context context) {
        items = new ArrayList<>();
        items.add(new Sighting(1, "Cu", "Кучевые", "над парком"));
        items.add(new Sighting(2, "Ci", "Перистые", "утром"));
    }

    @Override
    public boolean save(Sighting sighting) {
        items.add(sighting);
        return true;
    }

    @Override
    public List<Sighting> getAll() {
        return items;
    }
}
