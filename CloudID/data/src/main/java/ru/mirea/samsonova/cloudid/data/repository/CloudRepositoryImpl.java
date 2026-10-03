package ru.mirea.samsonova.cloudid.data.repository;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.samsonova.cloudid.data.network.NetworkApi;
import ru.mirea.samsonova.cloudid.domain.models.CloudType;
import ru.mirea.samsonova.cloudid.domain.repository.CloudRepository;

public class CloudRepositoryImpl implements CloudRepository {
    private final NetworkApi networkApi;

    public CloudRepositoryImpl(NetworkApi networkApi) {
        this.networkApi = networkApi;
    }

    @Override
    public List<CloudType> getCatalog() {
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

    @Override
    public CloudType getDetails(String code) {
        for (CloudType type : getCatalog()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        List<CloudType> catalog = getCatalog();
        return catalog.isEmpty() ? null : catalog.get(0);
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
