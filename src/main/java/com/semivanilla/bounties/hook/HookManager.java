package com.semivanilla.bounties.hook;

import com.semivanilla.bounties.Bounties;
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
        if(plugin.getConfiguration().isUseFightingXP() && plugin.getServer().getPluginManager().isPluginEnabled("AureliumSkills")){
            XPImpl = new AureliumSkills(this);
        }else {
            XPImpl = new VanillaSkills(this);
            plugin.getLogger().info("Reward system has been set to use Vanilla XP as rewards");
        }
    }

    public SkillImpl getXPImpl() {
        return XPImpl;
    }

    public Bounties getPlugin() {
        return plugin;
    }
}
