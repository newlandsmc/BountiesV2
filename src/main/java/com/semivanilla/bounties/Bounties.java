package com.semivanilla.bounties;


import com.semivanilla.bounties.api.BountiesAPI;
import com.semivanilla.bounties.commands.CommandHandler;
import com.semivanilla.bounties.config.Configuration;
import com.semivanilla.bounties.gui.core.GUIHandler;
import com.semivanilla.bounties.hook.HookManager;
import com.semivanilla.bounties.listener.PlayerConnectionListener;
import com.semivanilla.bounties.listener.PlayerDeathListener;
import com.semivanilla.bounties.storage.core.DatabaseHandler;
import com.semivanilla.bounties.utils.UtilityManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bounties extends JavaPlugin {
    private UtilityManager utilityManager;
    private Configuration configuration;
    private DatabaseHandler databaseHandler;
    private PluginDataManager dataManager;
    private HookManager hookManager;
    private CommandHandler commandHandler;
    private GUIHandler guiHandler;

    private static BountiesAPI api = null;

    @Override
    public void onEnable() {
        this.utilityManager = new UtilityManager(this);
        this.configuration = new Configuration(this);
        this.databaseHandler = new DatabaseHandler(this);
        this.dataManager = new PluginDataManager(this);
        this.hookManager = new HookManager(this);
        this.commandHandler = new CommandHandler(this);
        this.guiHandler = new GUIHandler(this);

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

        hookManager.initHooks();
        if(!hookManager.getXPImpl().hookWithAPI()){
            getLogger().severe("Unable to connect properly with XP Provider!, the plugin will be disabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this),this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this),this);

        commandHandler.registerOthers();
        commandHandler.registerCommands();
        //Load the API Atlast
        api = new PluginAPI(this);

    }

    public void reloadPlugin(){


        if(!configuration.initConfiguration()){
            getLogger().severe("Unable to instantiate configuration. The plugin will be disabled!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        configuration.loadConfiguration();

        hookManager.initHooks();
        if(!hookManager.getXPImpl().hookWithAPI()){
            getLogger().severe("Unable to connect properly with XP Provider!, the plugin will be disabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
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

    public HookManager getHookManager() {
        return hookManager;
    }

    public static BountiesAPI getBountyAPI() throws IllegalAccessException {
        if(api == null)
            throw new IllegalAccessException("The plugin/API is not yet initialized!");

        return api;
    }

    public GUIHandler getGuiHandler() {
        return guiHandler;
    }
}
