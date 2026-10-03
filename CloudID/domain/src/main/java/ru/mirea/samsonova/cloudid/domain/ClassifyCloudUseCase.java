package ru.mirea.samsonova.cloudid.domain;

import java.util.List;

import ru.mirea.samsonova.cloudid.domain.models.Classification;
import ru.mirea.samsonova.cloudid.domain.repository.CloudClassifierRepository;

/** Классифицировать кадр моделью */
public class ClassifyCloudUseCase {
    private CloudClassifierRepository cloudClassifierRepository;

    public ClassifyCloudUseCase(CloudClassifierRepository cloudClassifierRepository) {
        this.cloudClassifierRepository = cloudClassifierRepository;
    }

    public List<Classification> execute(float[] nhwc224) {
        return cloudClassifierRepository.classify(nhwc224);
    }
}
