package ru.mirea.samsonova.cloudid.domain;

import java.util.List;

import ru.mirea.samsonova.cloudid.domain.models.CloudType;
import ru.mirea.samsonova.cloudid.domain.repository.CloudRepository;

/** Просмотреть каталог родов */
public class GetCloudCatalogUseCase {
    private CloudRepository cloudRepository;

    public GetCloudCatalogUseCase(CloudRepository cloudRepository) {
        this.cloudRepository = cloudRepository;
    }

    public List<CloudType> execute() {
        return cloudRepository.getCatalog();
    }
}
