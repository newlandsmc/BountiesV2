package com.semivanilla.bounties.listener;

import com.semivanilla.bounties.Bounties;
import net.badbird5907.anticombatlog.manager.NPCManager;
import net.badbird5907.anticombatlog.object.CombatNPCTrait;
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
        final UUID deadPlayerUID;
        if(event.getEntity() == null){
            if(event.getEntity().hasMetadata("NPC") && NPCManager.getNPCRegistry().getNPC(event.getEntity()).hasTrait(CombatNPCTrait.class)){
                CombatNPCTrait trait = NPCManager.getNPCRegistry().getNPC(event.getEntity()).getTraitNullable(CombatNPCTrait.class);
                deadPlayerUID = trait.getUuid();
            }else {
                //This isn't a combat logged situation too, it wouldn't happen, but to avoid NPE, we can just return it
                //from here
                return;
            }
        } else {
            deadPlayerUID = event.getEntity().getUniqueId();
        }

        //If there is no killer, it means the player died with something else than PvP
        if(event.getEntity().getKiller() == null)
            return;

        final UUID killerUID = event.getEntity().getKiller().getUniqueId();

        //Check if the kill is duplicated
        if(plugin.getDataManager().getPlayerTrackerManager().isDuplicatedKill(killerUID,deadPlayerUID)){
            //TODO Add Messages for this
            return;
        }

        //Check if they have permission to exempt from bounty
        if(event.getEntity().getKiller().hasPermission("bounty.bypass") || event.getEntity().hasPermission("bounty.bypass"))
            return;

        if(plugin.getDataManager().isPlayerExempted(killerUID) || plugin.getDataManager().isPlayerExempted(deadPlayerUID))
            return;

        event.setDeathMessage(null);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if(plugin.getDataManager().isPlayerBounty(deadPlayerUID)){
            //TODO Add bounty player kill to the player
        }

    }
}
