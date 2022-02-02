package com.semivanilla.bounties;

import com.semivanilla.bounties.api.BountiesAPI;
import com.semivanilla.bounties.config.Configuration;
import com.semivanilla.bounties.storage.core.DatabaseHandler;
import com.semivanilla.bounties.utils.UtilityManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bounties extends JavaPlugin {
    private UtilityManager utilityManager;
    private Configuration configuration;
    private DatabaseHandler databaseHandler;
    private PluginDataManager dataManager;

    private static BountiesAPI api = null;

    @Override
    public void onEnable() {
        this.utilityManager = new UtilityManager(this);
        this.configuration = new Configuration(this);
        this.databaseHandler = new DatabaseHandler(this);
        this.dataManager = new PluginDataManager(this);

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

        //Load the API Atlast
        api = new PluginAPI(this);
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

    public static BountiesAPI getBountyAPI() throws IllegalAccessException {
        if(api == null)
            throw new IllegalAccessException("The plugin/API is not yet initialized!");
        
        return api;
    }
}
