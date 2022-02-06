package com.semivanilla.bounties.config;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.gui.core.buttons.Buttons;
import com.semivanilla.bounties.gui.core.buttons.Fillers;
import com.semivanilla.bounties.utils.modules.ItemUtils;
import com.semivanilla.bounties.utils.modules.MessageFormatter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public final class Configuration {

    private final Bounties plugin;
    private FileConfiguration configuration;

    private long bountyDuration;
    private long playerKillTagDuration;

    private boolean useFightingXP;
    private int XPForNonBountyPlayer;

    private final HashMap<Integer,Integer> bountyKillReward = new HashMap<>();
    private final TreeSet<Integer> bountyKillMap = new TreeSet<>();

    private final HashMap<Integer, Integer> bountyKillRandomLocRadius = new HashMap<>();
    private final TreeSet<Integer> bountyKillRandomLocMap = new TreeSet<>();

    private String formattedPlaceholderBountyTag,formattedPlaceholderZeroOnline,formattedPlaceholderPlayersOnline;

    private int messageDelay;
    private List<String> messageBroadcastNewBounty, messageBroadcastBountyGrows,messageBroadcastBountyClaimed,messagePlayerSpamKiller,messagePlayerSpamVictim;

    private List<String> messageHelpHeader,messageHelpFooter;
    private String messageHelpContent;

    private boolean showNextAndPreOnlyNeeded;
    private String bountyMenuName;
    private int bountyMenuRows;
    private Buttons bountyMenuPreviousButtons, bountyMenuNextButtons, bountyMenuBountyButton,bountyMenuStatsButton;
    private final List<Fillers> bountyMenuFillers;

    public Configuration(Bounties plugin) {
        this.plugin = plugin;
        this.bountyMenuFillers = new ArrayList<>();
    }

    public boolean initConfiguration(){
        this.plugin.saveDefaultConfig();
        this.plugin.reloadConfig();
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

        this.bountyKillRandomLocMap.clear();
        this.bountyKillRandomLocRadius.clear();
        this.configuration.getConfigurationSection("rand-location-radius-for-kills").getKeys(false).forEach((kills) -> {
            bountyKillRandomLocRadius.put(Integer.parseInt(kills),configuration.getInt("rand-location-radius-for-kills."+kills));
        });
        this.bountyKillRandomLocMap.addAll(bountyKillRandomLocRadius.keySet().stream().toList());

        this.formattedPlaceholderBountyTag = MessageFormatter.colorizeLegacy(this.configuration.getString("hook.placeholder-api.formatted-tag-for-bounty"));
        this.formattedPlaceholderZeroOnline = MessageFormatter.colorizeLegacy(this.configuration.getString("hook.placeholder-api.formatted-online-count.zero-online"));
        this.formattedPlaceholderPlayersOnline = MessageFormatter.colorizeLegacy(this.configuration.getString("hook.placeholder-api.formatted-online-count.online"));

        this.messageDelay = this.configuration.getInt("messages.interval-in-sec-to-send-message");
        this.messageBroadcastNewBounty = this.configuration.getStringList("messages.new-bounty-broadcast");
        this.messageBroadcastBountyGrows = this.configuration.getStringList("messages.existing-bounty-broadcast");
        this.messageBroadcastBountyClaimed = this.configuration.getStringList("messages.player-bounty-released-broadcast");
        this.messagePlayerSpamKiller = this.configuration.getStringList("messages.already-killed-before.killer");
        this.messagePlayerSpamVictim = this.configuration.getStringList("messages.already-killed-before.victim");

        this.messageHelpHeader = this.configuration.getStringList("messages.help-message.header");
        this.messageHelpContent = this.configuration.getString("messages.help-message.command-description");
        this.messageHelpFooter = this.configuration.getStringList("messages.help-message.footer");

        this.showNextAndPreOnlyNeeded = this.configuration.getBoolean("gui.bounty-menu.show-next-and-pre-if-only-needed");
        this.bountyMenuName = this.configuration.getString("gui.bounty-menu.name");
        this.bountyMenuRows = this.configuration.getInt("gui.bounty-menu.row");
        this.bountyMenuPreviousButtons = Buttons.buildButtons(Objects.requireNonNull(this.configuration.getConfigurationSection("gui.bounty-menu.buttons.pre-button")));
        this.bountyMenuNextButtons = Buttons.buildButtons(Objects.requireNonNull(this.configuration.getConfigurationSection("gui.bounty-menu.buttons.next-button")));
        this.bountyMenuBountyButton = Buttons.buildButtons(Objects.requireNonNull(this.configuration.getConfigurationSection("gui.bounty-menu.buttons.bounty-button")));
        this.bountyMenuStatsButton = Buttons.buildButtons(Objects.requireNonNull(this.configuration.getConfigurationSection("gui.bounty-menu.buttons.info-button")));
        this.bountyMenuFillers.clear();
        this.configuration.getConfigurationSection("gui.bounty-menu.filler").getKeys(false).forEach(item -> {
            final ItemStack fillerMaterial = ItemUtils.getMaterialFrom(item);
            final List<String> fillerList = this.configuration.getStringList("gui.bounty-menu.filler."+item);
            bountyMenuFillers.add(Fillers.buildFrom(fillerMaterial,fillerList));
        });

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

    public int getRadiusForRandLocOnKills(int kills){
        if(bountyKillRandomLocRadius.containsKey(kills))
            return bountyKillRandomLocRadius.get(kills);

        else return bountyKillRandomLocRadius.get(bountyKillRandomLocMap.lower(kills));
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

    public List<String> getMessageHelpHeader() {
        return messageHelpHeader;
    }

    public List<String> getMessageHelpFooter() {
        return messageHelpFooter;
    }

    public String getMessageHelpContent(String command,String desc) {
        return messageHelpContent.replace("%command%",command).replace("%description%",desc);
    }

    public boolean isShowNextAndPreOnlyNeeded() {
        return showNextAndPreOnlyNeeded;
    }

    public String getBountyMenuName() {
        return bountyMenuName;
    }

    public int getBountyMenuRows() {
        return bountyMenuRows;
    }

    public Buttons getBountyMenuPreviousButtons() {
        return bountyMenuPreviousButtons;
    }

    public Buttons getBountyMenuNextButtons() {
        return bountyMenuNextButtons;
    }

    public List<Fillers> getBountyMenuFillers() {
        return bountyMenuFillers;
    }

    public Buttons getBountyMenuBountyButton() {
        return bountyMenuBountyButton;
    }

    public Buttons getBountyMenuStatsButton() {
        return bountyMenuStatsButton;
    }
}
