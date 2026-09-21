package ru.mirea.samsonova.cloudid.domain;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.samsonova.cloudid.domain.models.Sighting;
import ru.mirea.samsonova.cloudid.domain.models.User;
import ru.mirea.samsonova.cloudid.domain.repository.AtlasRepository;
import ru.mirea.samsonova.cloudid.domain.repository.AuthRepository;

/** Просмотреть мой атлас */
public class GetMyAtlasUseCase {
    private AtlasRepository atlasRepository;
    private AuthRepository authRepository;

    public GetMyAtlasUseCase(AtlasRepository atlasRepository, AuthRepository authRepository) {
        this.atlasRepository = atlasRepository;
        this.authRepository = authRepository;
    }

    public List<Sighting> execute() {
        User user = authRepository.getProfile();
        if (user.isGuest()) {
            return new ArrayList<>();
        }
        return atlasRepository.getAll();
    }
}
