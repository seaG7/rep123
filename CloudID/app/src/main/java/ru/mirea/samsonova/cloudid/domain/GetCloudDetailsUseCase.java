package ru.mirea.samsonova.cloudid.domain;

import ru.mirea.samsonova.cloudid.domain.models.CloudType;
import ru.mirea.samsonova.cloudid.domain.repository.CloudRepository;

/** Открыть карточку рода */
public class GetCloudDetailsUseCase {
    private CloudRepository cloudRepository;

    public GetCloudDetailsUseCase(CloudRepository cloudRepository) {
        this.cloudRepository = cloudRepository;
    }

    public CloudType execute(String code) {
        return cloudRepository.getDetails(code);
    }
}
