package ru.mirea.samsonova.cloudid.data.repository;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import ru.mirea.samsonova.cloudid.data.network.NetworkApi;
import ru.mirea.samsonova.cloudid.data.network.WikiApi;
import ru.mirea.samsonova.cloudid.data.network.WikiClient;
import ru.mirea.samsonova.cloudid.data.network.WikiSummary;
import ru.mirea.samsonova.cloudid.domain.models.CloudType;
import ru.mirea.samsonova.cloudid.domain.repository.CloudRepository;

public class CloudRepositoryImpl implements CloudRepository {
    private static final Map<String, String> WIKI_TITLES = new LinkedHashMap<>();

    static {
        WIKI_TITLES.put("Cu", "Кучевые_облака");
        WIKI_TITLES.put("Ci", "Перистые_облака");
        WIKI_TITLES.put("Cb", "Кучево-дождевые_облака");
        WIKI_TITLES.put("Sc", "Слоисто-кучевые_облака");
        WIKI_TITLES.put("St", "Слоистые_облака");
        WIKI_TITLES.put("Ac", "Высококучевые_облака");
        WIKI_TITLES.put("As", "Высокослоистые_облака");
        WIKI_TITLES.put("Ns", "Слоисто-дождевые_облака");
        WIKI_TITLES.put("Cc", "Перисто-кучевые_облака");
        WIKI_TITLES.put("Cs", "Перисто-слоистые_облака");
        WIKI_TITLES.put("Ct", "Инверсионный_след");
    }

    private final NetworkApi networkApi;
    private final WikiApi wikiApi;
    private List<CloudType> cache;
    private String warning = "";

    public CloudRepositoryImpl(NetworkApi networkApi) {
        this(networkApi, WikiClient.create());
    }

    public CloudRepositoryImpl(NetworkApi networkApi, WikiApi wikiApi) {
        this.networkApi = networkApi;
        this.wikiApi = wikiApi;
    }

    @Override
    public List<CloudType> getCatalog() {
        List<CloudType> ready = readCache();
        if (ready != null) {
            return ready;
        }
        List<CloudType> stubs = readStub();
        List<CloudType> merged = new ArrayList<>();
        int failed = 0;
        for (CloudType stub : stubs) {
            CloudType remote = loadOne(stub);
            if (remote == null) {
                failed++;
                merged.add(stub);
            } else {
                merged.add(remote);
            }
        }
        if (stubs.isEmpty()) {
            warning = "Каталог пуст.";
        } else if (failed == stubs.size()) {
            warning = "Сеть не ответила. Показан сохранённый каталог.";
        } else if (failed > 0) {
            warning = "Не все карточки пришли из Wikipedia. Остальные взяты из сохранённого каталога.";
        } else {
            warning = "";
        }
        synchronized (this) {
            cache = merged;
        }
        return merged;
    }

    @Override
    public String lastWarning() {
        return warning == null ? "" : warning;
    }

    @Override
    public CloudType getDetails(String code) {
        List<CloudType> source = readCache();
        if (source == null) {
            source = readStub();
        }
        for (CloudType type : source) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return source.isEmpty() ? null : source.get(0);
    }

    private synchronized List<CloudType> readCache() {
        return cache;
    }

    private CloudType loadOne(CloudType stub) {
        String title = WIKI_TITLES.get(stub.getCode());
        if (title == null) {
            return null;
        }
        try {
            Response<WikiSummary> response = wikiApi.summary(title).execute();
            if (!response.isSuccessful() || response.body() == null) {
                return null;
            }
            WikiSummary summary = response.body();
            String extract = summary.extract == null || summary.extract.trim().isEmpty()
                    ? stub.getExtract()
                    : summary.extract.trim();
            String image = stub.getImageUrl();
            if (summary.thumbnail != null && summary.thumbnail.source != null
                    && !summary.thumbnail.source.isEmpty()) {
                image = summary.thumbnail.source;
            }
            return new CloudType(stub.getCode(), stub.getName(), stub.getLatin(), image, extract);
        } catch (IOException exception) {
            return null;
        }
    }

    private List<CloudType> readStub() {
        List<CloudType> catalog = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(networkApi.getCatalogJson());
            for (int i = 0; i < array.length(); i++) {
                catalog.add(mapToDomain(array.getJSONObject(i)));
            }
        } catch (Exception ignored) {
        }
        return catalog;
    }

    private CloudType mapToDomain(JSONObject object) throws Exception {
        return new CloudType(
                object.getString("code"),
                object.getString("name"),
                object.optString("latin", ""),
                object.optString("image", ""),
                object.optString("extract", "")
        );
    }
}
