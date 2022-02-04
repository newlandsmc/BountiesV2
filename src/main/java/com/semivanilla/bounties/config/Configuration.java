package com.semivanilla.bounties.config;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.utils.modules.MessageFormatter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public final class Configuration {

    private final Bounties plugin;
    private FileConfiguration configuration;

    private long bountyDuration;
    private long playerKillTagDuration;

    private boolean useFightingXP;
    private int XPForNonBountyPlayer;

    private final HashMap<Integer,Integer> bountyKillReward = new HashMap<>();
    private final TreeSet<Integer> bountyKillMap = new TreeSet<>();

    private String formattedPlaceholderBountyTag,formattedPlaceholderZeroOnline,formattedPlaceholderPlayersOnline;

    private int messageDelay;
    private List<String> messageBroadcastNewBounty, messageBroadcastBountyGrows,messageBroadcastBountyClaimed,messagePlayerSpamKiller,messagePlayerSpamVictim;

    public Configuration(Bounties plugin) {
        this.plugin = plugin;
    }

    public boolean initConfiguration(){
        this.plugin.saveDefaultConfig();
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
        this.bountyKillReward.clear();
        this.configuration.getConfigurationSection("rewards.bounty-rewards").getKeys(false).forEach((kills) -> {
            bountyKillReward.put(Integer.parseInt(kills),configuration.getInt("rewards.bounty-rewards."+kills));
        });
        this.bountyKillMap.addAll(bountyKillReward.keySet().stream().toList());

        this.formattedPlaceholderBountyTag = MessageFormatter.colorizeLegacy(this.configuration.getString("hook.placeholder-api.formatted-tag-for-bounty"));
        this.formattedPlaceholderZeroOnline = MessageFormatter.colorizeLegacy(this.configuration.getString("hook.placeholder-api.formatted-online-count.zero-online"));
        this.formattedPlaceholderPlayersOnline = MessageFormatter.colorizeLegacy(this.configuration.getString("hook.placeholder-api.formatted-online-count.online"));

        this.messageDelay = this.configuration.getInt("messages.interval-in-sec-to-send-message");
        this.messageBroadcastNewBounty = this.configuration.getStringList("messages.new-bounty-broadcast");
        this.messageBroadcastBountyGrows = this.configuration.getStringList("messages.existing-bounty-broadcast");
        this.messageBroadcastBountyClaimed = this.configuration.getStringList("messages.player-bounty-released-broadcast");
        this.messagePlayerSpamKiller = this.configuration.getStringList("messages.already-killed-before.killer");
        this.messagePlayerSpamVictim = this.configuration.getStringList("messages.already-killed-before.victim");
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

    public String getFormattedPlaceholderBountyTag() {
        return formattedPlaceholderBountyTag;
    }

    public String getFormattedPlaceholderZeroOnline() {
        return formattedPlaceholderZeroOnline;
    }

    public int getMessageDelay() {
        return messageDelay;
    }

    public String getFormattedPlaceholderPlayersOnline(long onlineCount) {
        return this.formattedPlaceholderPlayersOnline.replace("%online%",String.valueOf(onlineCount));
    }

    public List<String> getMessageBroadcastNewBounty(String killerName, String deadPlayerName) {
        final List<String> placeholderParsedMessage = new ArrayList<>();
        this.messageBroadcastNewBounty.forEach(line ->{
            String message = line;
            message = message.replace("%killer%",killerName).replace("%dead_player%",deadPlayerName);
            placeholderParsedMessage.add(message);
        });
        return placeholderParsedMessage;
    }

    public List<String> getMessageBroadcastBountyGrows(String killerName, String deadPlayerName) {
        final List<String> placeholderParsedMessage = new ArrayList<>();
        this.messageBroadcastBountyGrows.forEach(line -> {
            String message = line;
            message = message.replace("%killer%",killerName).replace("%dead_player%",deadPlayerName);
            placeholderParsedMessage.add(message);
        });

        return placeholderParsedMessage;
    }

    public List<String> getMessageBroadcastBountyClaimed(String killerName, String deadPlayerName) {
        final List<String> placeholderParsedMessage = new ArrayList<>();
        this.messageBroadcastBountyClaimed.forEach(line -> {
            String message = line;
            message = message.replace("%killer%",killerName).replace("%dead_player%",deadPlayerName);
            placeholderParsedMessage.add(message);
        });
        return placeholderParsedMessage;
    }

    public List<String> getMessagePlayerSpamKiller() {
        return messagePlayerSpamKiller;
    }

    public List<String> getMessagePlayerSpamVictim() {
        return messagePlayerSpamVictim;
    }
}
