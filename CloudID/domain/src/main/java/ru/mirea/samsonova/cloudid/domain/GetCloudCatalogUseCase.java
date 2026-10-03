package ru.mirea.samsonova.cloudid.domain;

import ru.mirea.samsonova.cloudid.domain.models.CatalogLoad;
import ru.mirea.samsonova.cloudid.domain.repository.CloudRepository;

/** Просмотреть каталог родов */
public class GetCloudCatalogUseCase {
    private CloudRepository cloudRepository;

    public GetCloudCatalogUseCase(CloudRepository cloudRepository) {
        this.cloudRepository = cloudRepository;
    }

    public CatalogLoad execute() {
        return new CatalogLoad(cloudRepository.getCatalog(), cloudRepository.lastWarning());
    }
}
