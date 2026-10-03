package ru.mirea.samsonova.cloudid.data.storage.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SightingDao {
    @Query("SELECT * FROM sightings ORDER BY id DESC")
    List<SightingEntity> getAll();

    @Insert
    void insert(SightingEntity entity);
}
