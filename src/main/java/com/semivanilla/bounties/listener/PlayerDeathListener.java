package com.semivanilla.bounties.listener;

import com.semivanilla.bounties.Bounties;
import com.semivanilla.bounties.enums.QueueAction;
import com.semivanilla.bounties.model.PlayerTracker;
import com.semivanilla.bounties.utils.modules.InternalPlaceholders;
import net.badbird5907.anticombatlog.manager.NPCManager;
import net.badbird5907.anticombatlog.object.CombatNPCTrait;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public final class PlayerDeathListener implements Listener {

    private final Bounties plugin;

    public PlayerDeathListener(Bounties plugin) {
        this.plugin = plugin;
    }

    /**
     * Things to undertake on the event
     * 1 -> Check if the dead player is null, If its null obtain the real UID of the player with what BadBird Suggested
     * 2 -> Check if the killer is null, If its return the event as that means the situation was not a PvP
     * 3 -> Check if the Killer And DeadGuy is duplicated*
     * 4 -> Check if both of them are exempted from the Bounty
     */
    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event){
        boolean isDeadCombatLogged = false;
        boolean deadPlayerWasBounty = false;

        String deadName = null;
        final UUID deadPlayerUID;
        if(event.getEntity().hasMetadata("NPC") && NPCManager.getNPCRegistry().getNPC(event.getEntity()).hasTrait(CombatNPCTrait.class)){
                CombatNPCTrait trait = NPCManager.getNPCRegistry().getNPC(event.getEntity()).getTraitNullable(CombatNPCTrait.class);
                deadPlayerUID = trait.getUuid();
                deadName = trait.getRawName();
                isDeadCombatLogged = true;
        } else {
            deadPlayerUID = event.getEntity().getUniqueId();
            deadName = event.getEntity().getName();
        }

        //If there is no killer, it means the player died with something else than PvP
        if(event.getEntity().getKiller() == null)
            return;

        final Player killer = event.getEntity().getKiller();
        final UUID killerUID = event.getEntity().getKiller().getUniqueId();

        //If the player is combat logged...load the data of the player and then add the death and save again
        if(isDeadCombatLogged){
            plugin.getDatabaseHandler().getDataStorage().getOrRegister(deadPlayerUID).thenAccept((stats) -> {
               stats.addDeath();
               plugin.getDatabaseHandler().getDataStorage().savePlayerStatisticsAsync(stats);
            });
        }else {
            plugin.getDataManager().getStatisticsManager().addDeathForPlayer(deadPlayerUID);
        }

        //TODO Remove this after testing...I have only 1 acc to test :cry:

        //Check if the kill is duplicated
        if(plugin.getDataManager().getPlayerTrackerManager().isDuplicatedKill(killerUID,deadPlayerUID)){

            plugin.getUtilityManager().getMessagingUtils().sendInInterval(plugin.getServer().getPlayer(deadPlayerUID)
            ,plugin.getConfiguration().getMessagePlayerSpamVictim(),
                    plugin.getConfiguration().getMessageDelay(),
                    new InternalPlaceholders("%dead_player%",deadName),
                    new InternalPlaceholders("%killer%",killer.getName()));

            plugin.getUtilityManager().getMessagingUtils().sendInInterval(killer
                    ,plugin.getConfiguration().getMessagePlayerSpamKiller(),
                    plugin.getConfiguration().getMessageDelay(),
                    new InternalPlaceholders("%dead_player%",deadName),
                    new InternalPlaceholders("%killer%",killer.getName()));

            plugin.getDataManager().getPlayerTrackerManager().updateKillForDuplicatedKill(killerUID);
            plugin.getDataManager().getStatisticsManager().addKillForPlayer(killerUID);
            return;
        }


        final PlayerTracker killTracker = new PlayerTracker(killerUID,deadPlayerUID);
        plugin.getDataManager().getPlayerTrackerManager().addPlayerTracker(killTracker);

        //Check if they have permission to exempt from bounty
        if(event.getEntity().getKiller().hasPermission("bounty.bypass") || event.getEntity().hasPermission("bounty.bypass")) {
            plugin.getDataManager().getStatisticsManager().addKillForPlayer(killerUID);
            return;
        }

        if(plugin.getDataManager().isPlayerExempted(killerUID) || plugin.getDataManager().isPlayerExempted(deadPlayerUID)) {
            plugin.getDataManager().getStatisticsManager().addKillForPlayer(killerUID);
            return;
        }

        event.setDeathMessage(null);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //The dead-player is a bounty
        if(plugin.getDataManager().isPlayerBounty(deadPlayerUID)){
            deadPlayerWasBounty = true;
            //Adds a bounty kill to the killer
            plugin.getDataManager().getStatisticsManager().addBountyKillForPlayer(killerUID);

            final int xpToProcess = plugin.getConfiguration().getXPForKills(plugin.getDataManager().getBountyManager().getBounty(deadPlayerUID).getKilled());
            //If the player combat logged, Queue his XP Removal, add xp to player
            if(isDeadCombatLogged){
                plugin.getDataManager().getRewardQueueManager().registerNewRewardQueue(deadPlayerUID, QueueAction.REMOVE_XP,xpToProcess);
                plugin.getHookManager().getXPImpl().addXPForPlayer(killer,xpToProcess);
            }else {
                //If not, do the action immediately
                plugin.getHookManager().getXPImpl().addXPForPlayer(killer,xpToProcess);
                plugin.getHookManager().getXPImpl().removeXPForPlayer(event.getEntity().getPlayer(),xpToProcess);
            }

            //Remove the bounty on his head
            plugin.getDataManager().getBountyManager().clearBountyOn(deadPlayerUID);
            plugin.getUtilityManager().getMessagingUtils().queueBroadcast(plugin.getConfiguration().getMessageBroadcastBountyClaimed(killer.getName(),deadName),
                    plugin.getConfiguration().getMessageDelay());

        }else {
            //The dead player is not a bounty, But there are still rewards.
            //Execute the reward, If the deadplayer is combat logged in this situation, queue the xp to add
            if(isDeadCombatLogged){
                plugin.getDataManager().getRewardQueueManager().registerNewRewardQueue(deadPlayerUID, QueueAction.REMOVE_XP,plugin.getConfiguration().getXPForNonBountyPlayer());
                plugin.getHookManager().getXPImpl().addXPForPlayer(killer,plugin.getConfiguration().getXPForNonBountyPlayer());
            }else {
                //If not combat logged, process to process XP
                plugin.getHookManager().getXPImpl().addXPForPlayer(killer,plugin.getConfiguration().getXPForNonBountyPlayer());
                plugin.getHookManager().getXPImpl().removeXPForPlayer(event.getEntity().getPlayer(),plugin.getConfiguration().getXPForNonBountyPlayer());
            }
        }

        //If the dead player was a bounty, we don't want to create a new bounty nor add kill to the killer, even if he is a bounty
        if(deadPlayerWasBounty)
            return;

        //If the dead player is not bounty
        // and if the killer is a bounty, that means he killed another non- bounty player, so update kill on him
        if(plugin.getDataManager().getBountyManager().isBounty(killerUID)){
            plugin.getDataManager().getBountyManager().updateKillOn(killerUID);
            plugin.getUtilityManager().getMessagingUtils().queueBroadcast(plugin.getConfiguration().getMessageBroadcastBountyGrows(killer.getName(), deadName),
                    plugin.getConfiguration().getMessageDelay());
        }else {
            //If the killer is not a bounty, that means his head should have a bounty
            plugin.getDataManager().getBountyManager().createBountyOn(killerUID);
            plugin.getUtilityManager().getMessagingUtils().queueBroadcast(plugin.getConfiguration().getMessageBroadcastNewBounty(killer.getName(), deadName),
                    plugin.getConfiguration().getMessageDelay());

        }

    }
}
