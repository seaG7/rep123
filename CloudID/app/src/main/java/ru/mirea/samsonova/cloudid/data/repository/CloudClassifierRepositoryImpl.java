package ru.mirea.samsonova.cloudid.data.repository;

import android.content.Context;

import ru.mirea.samsonova.cloudid.domain.models.Classification;
import ru.mirea.samsonova.cloudid.domain.repository.CloudClassifierRepository;

public class CloudClassifierRepositoryImpl implements CloudClassifierRepository {
    public CloudClassifierRepositoryImpl(Context context) {
    }

    @Override
    public Classification classify() {
        return new Classification("Cu", "Cumulus", 0.99f);
    }
}
