package ru.mirea.samsonova.cloudid.data.storage.client;

import ru.mirea.samsonova.cloudid.data.storage.client.models.ClientRecord;

public interface ClientStorage {
    ClientRecord get();

    boolean save(ClientRecord record);
}
