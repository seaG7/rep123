package ru.mirea.samsonova.cloudid.data.storage.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SightingDao {
    @Query("SELECT * FROM sightings ORDER BY id DESC")
    List<SightingEntity> getAll();

    @Query("SELECT * FROM sightings ORDER BY id DESC")
    LiveData<List<SightingEntity>> observeAll();

    @Insert
    void insert(SightingEntity entity);
}
