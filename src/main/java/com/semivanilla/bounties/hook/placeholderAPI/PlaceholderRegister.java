package com.semivanilla.bounties.hook.placeholderAPI;

import com.semivanilla.bounties.hook.HookManager;
import com.semivanilla.bounties.model.Bounty;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PlaceholderRegister extends PlaceholderExpansion {

    private final HookManager manager;

    public PlaceholderRegister(HookManager manager) {
        this.manager = manager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "bs";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Alen_Alex";
    }

    @Override
    public @NotNull String getVersion() {
        return manager.getPlugin().getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        /**
         * Returns whether the given player is a bounty or not
         */
        if(params.equalsIgnoreCase("isbounty")){
            return String.valueOf(manager.getPlugin().getDataManager().isPlayerBounty(player));
        }

        /**
         * Returns the no of kills a bounty have.
         * NOTE: Will return 0, if this is not a bounty
         */
        if(params.equalsIgnoreCase("kills")){
            if(manager.getPlugin().getDataManager().isPlayerBounty(player)){
                return String.valueOf(manager.getPlugin().getDataManager().getBountyManager().getBounty(player).getKilled());
            }else {
                return "0";
            }
        }

        /**
         * Returns the time remaining of how much time the bounty tag will be left on the player
         * NOTE: Will return 0, if this is not a bounty
         */
        if(params.equalsIgnoreCase("timeleft")){
            if(manager.getPlugin().getDataManager().isPlayerBounty(player)){
                return manager.getPlugin().getDataManager().getBountyManager().getBounty(player).getFormattedRemaining();
            }else {
                return "0";
            }
        }

        /**
         * Returns the no of online bounty the server has
         */
        if(params.equalsIgnoreCase("online")){
            return String.valueOf(manager.getPlugin().getDataManager().getBountyManager().getBountiesHashMap().values().stream().filter(Bounty::isPlayerOnline).count());
        }

        /**
         * Returns the no of online bounty within the configuration formatted way
         */
        if(params.equalsIgnoreCase("online_formatted")){
            final long count = manager.getPlugin().getDataManager().getBountyManager().getBountiesHashMap().values().stream().filter(Bounty::isPlayerOnline).count();
            if(count > 0){
                return manager.getPlugin().getConfiguration().getFormattedPlaceholderPlayersOnline(count);
            }else return manager.getPlugin().getConfiguration().getFormattedPlaceholderZeroOnline();
        }

        /**
         * Returns the xp that a player gets when the bounty gets killed
         */
        if(params.equalsIgnoreCase("xp_worth")){
            if(manager.getPlugin().getDataManager().isPlayerBounty(player)){
                return String.valueOf(manager.getPlugin().getConfiguration().getXPForKills(manager.getPlugin().getDataManager().getBountyManager().getBounty(player).getKilled()));
            }else {
                return "0";
            }
        }

        /**
         * Returns a tag if a player is bounty
         */
        if(params.equalsIgnoreCase("tag")){
            if(manager.getPlugin().getDataManager().isPlayerBounty(player))
                return manager.getPlugin().getConfiguration().getFormattedPlaceholderBountyTag();
            else return "";
        }


        return null;
    }
}
