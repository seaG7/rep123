package ru.mirea.samsonova.cloudid.data.repository;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.samsonova.cloudid.domain.models.CloudType;
import ru.mirea.samsonova.cloudid.domain.repository.CloudRepository;

public class CloudRepositoryImpl implements CloudRepository {
    private final List<CloudType> catalog;

    public CloudRepositoryImpl(Context context) {
        catalog = new ArrayList<>();
        catalog.add(new CloudType("Cu", "Кучевые", "Cumulus",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3c/GoldenMedows.jpg/330px-GoldenMedows.jpg",
                "Плотные облака с плоским основанием, похожи на хлопья ваты."));
        catalog.add(new CloudType("Ci", "Перистые", "Cirrus",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2e/CirrusField-color.jpg/330px-CirrusField-color.jpg",
                "Высокие тонкие облака из ледяных кристаллов."));
        catalog.add(new CloudType("Cb", "Кучево-дождевые", "Cumulonimbus",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/8/85/Cumulonimbus_incus_over_Warsaw%2C_Poland.jpg/330px-Cumulonimbus_incus_over_Warsaw%2C_Poland.jpg",
                "Мощные вертикальные облака, гроза и ливень."));
        catalog.add(new CloudType("Sc", "Слоисто-кучевые", "Stratocumulus",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4b/Above_the_Clouds.jpg/330px-Above_the_Clouds.jpg",
                "Низкие серые гряды и волны облаков."));
        catalog.add(new CloudType("St", "Слоистые", "Stratus",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/8/82/Stratus-Clouds.jpg/330px-Stratus-Clouds.jpg",
                "Серый однородный слой низких облаков."));
        catalog.add(new CloudType("Ac", "Высококучевые", "Altocumulus",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6b/Altocumulus.jpg/330px-Altocumulus.jpg",
                "Белые или серые волны в средней тропосфере."));
        catalog.add(new CloudType("As", "Высокослоистые", "Altostratus",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4e/Altostratus.jpg/330px-Altostratus.jpg",
                "Молочно-серый слой, солнце просвечивает слабо."));
        catalog.add(new CloudType("Ns", "Слоисто-дождевые", "Nimbostratus",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9a/Nimbostratus.jpg/330px-Nimbostratus.jpg",
                "Тёмный сплошной слой, обложной дождь или снег."));
        catalog.add(new CloudType("Cc", "Перисто-кучевые", "Cirrocumulus",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a3/Cirrocumulus.jpg/330px-Cirrocumulus.jpg",
                "Мелкие белые хлопья высоко в небе."));
        catalog.add(new CloudType("Cs", "Перисто-слоистые", "Cirrostratus",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5e/Cirrostratus.jpg/330px-Cirrostratus.jpg",
                "Белёсая вуаль, вокруг солнца бывает гало."));
        catalog.add(new CloudType("Ct", "Инверсионный след", "Contrail",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/0/00/Contrail.jpg/330px-Contrail.jpg",
                "Искусственное облако за самолётом."));
    }

    @Override
    public List<CloudType> getCatalog() {
        return catalog;
    }

    @Override
    public CloudType getDetails(String code) {
        for (CloudType type : catalog) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return catalog.get(0);
    }
}
