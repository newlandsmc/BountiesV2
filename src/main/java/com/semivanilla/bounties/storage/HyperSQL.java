package com.semivanilla.bounties.storage;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.storage.core.AbstractSQL;
import com.semivanilla.bounties.storage.core.DataStorageImpl;
import com.semivanilla.bounties.storage.core.DatabaseHandler;

import java.io.File;

public class HyperSQL extends AbstractSQL  {

    private final DatabaseHandler databaseHandler;

    public HyperSQL(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    public boolean initStorageConnection() {
        if(checkForClass("org.hsqldb.jdbc.JDBCDriver")){
            databaseHandler.getPlugin().getLogger().severe("JDBC Class is not present, This plugin won't be able to connect to HSQLDB.");
            return false;
        }

        final File file = new File(databaseHandler.getPlugin().getDataFolder()+File.separator+"data-storage","database");
        return connect("hsqldb",file.getAbsolutePath());
    }

    public void prepareDatabaseTables() {

    }

    public void close() {

    }

    public String storageType() {
        return null;
    }
}
