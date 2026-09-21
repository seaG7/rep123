package ru.mirea.samsonova.cloudid.domain;

import ru.mirea.samsonova.cloudid.domain.models.Classification;
import ru.mirea.samsonova.cloudid.domain.models.CloudType;
import ru.mirea.samsonova.cloudid.domain.repository.CloudClassifierRepository;
import ru.mirea.samsonova.cloudid.domain.repository.CloudRepository;

/** Определить облако по фото */
public class IdentifyCloudUseCase {
    private CloudClassifierRepository cloudClassifierRepository;
    private CloudRepository cloudRepository;

    public IdentifyCloudUseCase(CloudClassifierRepository cloudClassifierRepository,
                                CloudRepository cloudRepository) {
        this.cloudClassifierRepository = cloudClassifierRepository;
        this.cloudRepository = cloudRepository;
    }

    public CloudType execute() {
        Classification classification = cloudClassifierRepository.classify();
        return cloudRepository.getDetails(classification.getCode());
    }
}
