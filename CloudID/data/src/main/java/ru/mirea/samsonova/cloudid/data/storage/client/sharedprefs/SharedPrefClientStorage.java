package ru.mirea.samsonova.cloudid.data.storage.client.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

import ru.mirea.samsonova.cloudid.data.storage.client.ClientStorage;
import ru.mirea.samsonova.cloudid.data.storage.client.models.ClientRecord;

public class SharedPrefClientStorage implements ClientStorage {
    private static final String PREFS = "cloudid_client";
    private static final String KEY_ID = "id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_GUEST = "guest";
    private static final String KEY_DATE = "date";

    private final SharedPreferences sharedPreferences;

    public SharedPrefClientStorage(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    @Override
    public ClientRecord get() {
        return new ClientRecord(
                sharedPreferences.getInt(KEY_ID, 0),
                sharedPreferences.getString(KEY_EMAIL, "guest"),
                sharedPreferences.getBoolean(KEY_GUEST, true),
                sharedPreferences.getString(KEY_DATE, "")
        );
    }

    @Override
    public boolean save(ClientRecord record) {
        sharedPreferences.edit()
                .putInt(KEY_ID, record.getId())
                .putString(KEY_EMAIL, record.getEmail())
                .putBoolean(KEY_GUEST, record.isGuest())
                .putString(KEY_DATE, record.getLocalDate())
                .commit();
        return true;
    }
}
