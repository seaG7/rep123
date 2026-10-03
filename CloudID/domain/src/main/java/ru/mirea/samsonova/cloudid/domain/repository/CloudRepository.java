package ru.mirea.samsonova.cloudid.domain.repository;

import java.util.List;

import ru.mirea.samsonova.cloudid.domain.models.CloudType;

public interface CloudRepository {
    List<CloudType> getCatalog();

    CloudType getDetails(String code);
}
