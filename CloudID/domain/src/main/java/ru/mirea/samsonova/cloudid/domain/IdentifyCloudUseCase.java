package ru.mirea.samsonova.cloudid.domain;

import java.util.List;

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

    public CloudType execute(float[] nhwc224) {
        List<Classification> top = cloudClassifierRepository.classify(nhwc224);
        String code = top.isEmpty() ? "Cu" : top.get(0).getCode();
        return cloudRepository.getDetails(code);
    }
}
