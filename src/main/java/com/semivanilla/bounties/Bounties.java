package com.semivanilla.bounties;

import com.semivanilla.bounties.config.Configuration;
import com.semivanilla.bounties.storage.core.DatabaseHandler;
import com.semivanilla.bounties.utils.UtilityManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bounties extends JavaPlugin {
    private final UtilityManager utilityManager;
    private final Configuration configuration;
    private final DatabaseHandler databaseHandler;
    private final PluginDataManager dataManager;

    public Bounties() {
        this.utilityManager = new UtilityManager(this);
        this.configuration = new Configuration(this);
        this.databaseHandler = new DatabaseHandler(this);
        this.dataManager = new PluginDataManager(this);
    }

    @Override
    public void onEnable() {
        if(!configuration.initConfiguration()){
            getLogger().severe("Unable to instantiate configuration. The plugin will be disabled!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        configuration.loadConfiguration();

        if(!databaseHandler.initDatabaseConnection()){
            getLogger().severe("Unable to connect to database. The plugin will be disabled!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        databaseHandler.getDataStorage().prepareDatabaseTables();

        dataManager.loadAllBounties();
    }

    @Override
    public void onDisable() {
        databaseHandler.getDataStorage().close();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public UtilityManager getUtilityManager() {
        return utilityManager;
    }

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public PluginDataManager getDataManager() {
        return dataManager;
    }
}
