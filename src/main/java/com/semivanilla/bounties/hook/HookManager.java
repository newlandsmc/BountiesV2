package com.semivanilla.bounties.hook;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.hook.placeholderAPI.PlaceholderRegister;
import com.semivanilla.bounties.hook.skills.AureliumSkills;
import com.semivanilla.bounties.hook.skills.VanillaSkills;
import com.semivanilla.bounties.hook.skills.core.SkillImpl;

public class HookManager {

    private final Bounties plugin;

    private SkillImpl XPImpl;

    public HookManager(Bounties plugin) {
        this.plugin = plugin;
    }

    public void initHooks(){
        //Skills Hook
        if(plugin.getConfiguration().isUseFightingXP()){
            if(plugin.getServer().getPluginManager().isPluginEnabled("AureliumSkills"))
                XPImpl = new AureliumSkills(this);
            else {
                plugin.getLogger().warning("Configuration provided to use Aurelium Skills As XP Provider, but the plugin seems to be disabled!");
                XPImpl = new VanillaSkills(this);
            }
        }else {
            XPImpl = new VanillaSkills(this);
        }

        //PlaceholderAPI
        if(plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderRegister(this).register();
            plugin.getLogger().info("Successfully hooked with PlaceholderAPI");
        }
    }

    public SkillImpl getXPImpl() {
        return XPImpl;
    }

    public Bounties getPlugin() {
        return plugin;
    }
}
