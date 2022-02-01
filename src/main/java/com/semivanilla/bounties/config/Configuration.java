package com.semivanilla.bounties.config;

import com.semivanilla.bounties.Bounties;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.TreeSet;

public class Configuration {

    private final Bounties plugin;
    private FileConfiguration configuration;

    private long bountyDuration;
    private long playerKillTagDuration;

    private boolean useFightingXP;
    private int XPForNonBountyPlayer;

    private final HashMap<Integer,Integer> bountyKillReward = new HashMap<>();
    private final TreeSet<Integer> bountyKillMap = new TreeSet<>();

    public Configuration(Bounties plugin) {
        this.plugin = plugin;
    }

    public boolean initConfiguration(){
        this.plugin.saveConfig();
        this.configuration = this.plugin.getConfig();

        if(configuration == null) return false;
        else {
            configuration.addDefault("version", plugin.getDescription().getVersion());
            return true;
        }
    }

    public void loadConfiguration(){
        this.bountyDuration = this.configuration.getLong("tasks-duration.bounty-duration-in-sec");
        this.playerKillTagDuration = this.configuration.getLong("tasks-duration.player-kill-tag-in-sec");

        this.useFightingXP = this.configuration.getBoolean("rewards.use-fighting-xp");
        this.XPForNonBountyPlayer = this.configuration.getInt("rewards.reward-for-killing-non-bounty");

        this.bountyKillMap.clear();
        this.configuration.getConfigurationSection("rewards.bounty-rewards").getKeys(false).forEach((kills) -> {
            bountyKillReward.put(Integer.parseInt(kills),configuration.getInt("rewards.bounty-rewards."+kills));
        });
        this.bountyKillMap.addAll(bountyKillReward.values());
    }

    public long getBountyDuration() {
        return bountyDuration;
    }

    public long getPlayerKillTagDuration() {
        return playerKillTagDuration;
    }

    public boolean isUseFightingXP() {
        return useFightingXP;
    }

    public int getXPForNonBountyPlayer() {
        return XPForNonBountyPlayer;
    }

    public int getXPForKills(int kills){
        if(bountyKillReward.containsKey(kills))
            return bountyKillReward.get(kills);

        else return bountyKillReward.get(bountyKillMap.lower(kills));
    }
}
