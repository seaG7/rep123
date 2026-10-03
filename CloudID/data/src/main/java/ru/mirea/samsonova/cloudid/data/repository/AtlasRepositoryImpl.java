package ru.mirea.samsonova.cloudid.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.samsonova.cloudid.data.storage.room.AppDatabase;
import ru.mirea.samsonova.cloudid.data.storage.room.SightingDao;
import ru.mirea.samsonova.cloudid.data.storage.room.SightingEntity;
import ru.mirea.samsonova.cloudid.domain.models.Sighting;
import ru.mirea.samsonova.cloudid.domain.repository.AtlasRepository;

public class AtlasRepositoryImpl implements AtlasRepository {
    private final SightingDao sightingDao;

    public AtlasRepositoryImpl(Context context) {
        this.sightingDao = AppDatabase.getInstance(context).sightingDao();
    }

    @Override
    public boolean save(Sighting sighting) {
        sightingDao.insert(mapToStorage(sighting));
        return true;
    }

    @Override
    public List<Sighting> getAll() {
        List<Sighting> result = new ArrayList<>();
        for (SightingEntity entity : sightingDao.getAll()) {
            result.add(mapToDomain(entity));
        }
        return result;
    }

    public LiveData<List<Sighting>> observe() {
        return Transformations.map(sightingDao.observeAll(), entities -> {
            List<Sighting> result = new ArrayList<>();
            if (entities == null) {
                return result;
            }
            for (SightingEntity entity : entities) {
                result.add(mapToDomain(entity));
            }
            return result;
        });
    }

    private SightingEntity mapToStorage(Sighting sighting) {
        return new SightingEntity(
                0,
                sighting.getCloudCode(),
                sighting.getCloudName(),
                sighting.getNote(),
                sighting.getTakenAt(),
                sighting.getPhotoUri()
        );
    }

    private Sighting mapToDomain(SightingEntity entity) {
        return new Sighting(entity.id, entity.cloudCode, entity.cloudName, entity.note, entity.takenAt, entity.photoUri);
    }
}
