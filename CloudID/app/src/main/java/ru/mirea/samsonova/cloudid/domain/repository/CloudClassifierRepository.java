package ru.mirea.samsonova.cloudid.domain.repository;

import ru.mirea.samsonova.cloudid.domain.models.Classification;

public interface CloudClassifierRepository {
    Classification classify();
}
