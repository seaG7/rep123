package ru.mirea.samsonova.cloudid.domain.repository;

import java.util.List;

import ru.mirea.samsonova.cloudid.domain.models.Classification;

public interface CloudClassifierRepository {
    List<Classification> classify(float[] nhwc224);
}
