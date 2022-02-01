package com.semivanilla.bounties.storage.core;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.storage.H2;

public class DatabaseHandler {

    private final Bounties plugin;

    private DataStorageImpl dataStorage;

    public DatabaseHandler(Bounties plugin) {
        this.plugin = plugin;
    }

    public boolean initDatabaseConnection(){
        dataStorage = new H2(this);

        return dataStorage.initStorageConnection();
    }

    public DataStorageImpl getDataStorage() {
        return dataStorage;
    }

    public Bounties getPlugin() {
        return plugin;
    }
}
