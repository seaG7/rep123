package ru.mirea.samsonova.cloudid.domain;

import ru.mirea.samsonova.cloudid.domain.models.Sighting;
import ru.mirea.samsonova.cloudid.domain.models.User;
import ru.mirea.samsonova.cloudid.domain.repository.AtlasRepository;
import ru.mirea.samsonova.cloudid.domain.repository.AuthRepository;

/** Сохранить наблюдение */
public class SaveSightingUseCase {
    private AtlasRepository atlasRepository;
    private AuthRepository authRepository;

    public SaveSightingUseCase(AtlasRepository atlasRepository, AuthRepository authRepository) {
        this.atlasRepository = atlasRepository;
        this.authRepository = authRepository;
    }

    public boolean execute(Sighting sighting) {
        User user = authRepository.getProfile();
        if (user.isGuest()) {
            return false;
        }
        return atlasRepository.save(sighting);
    }
}
