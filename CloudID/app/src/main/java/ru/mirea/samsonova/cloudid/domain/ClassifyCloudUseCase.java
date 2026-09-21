package ru.mirea.samsonova.cloudid.domain;

import ru.mirea.samsonova.cloudid.domain.models.Classification;
import ru.mirea.samsonova.cloudid.domain.repository.CloudClassifierRepository;

/** Классифицировать кадр моделью */
public class ClassifyCloudUseCase {
    private CloudClassifierRepository cloudClassifierRepository;

    public ClassifyCloudUseCase(CloudClassifierRepository cloudClassifierRepository) {
        this.cloudClassifierRepository = cloudClassifierRepository;
    }

    public Classification execute() {
        return cloudClassifierRepository.classify();
    }
}
