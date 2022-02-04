package com.semivanilla.bounties.hook.skills;

import com.semivanilla.bounties.hook.HookManager;
import com.semivanilla.bounties.reward.core.XPImpl;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class AureliumXPProvider implements XPImpl {

    private final HookManager manager;
    private static final String COMMAND_FXP_ADD = "sk xp add %s fighting %d";
    private static final String COMMAND_FXP_REMOVE = "sk xp remove %s fighting %d";

    public AureliumXPProvider(HookManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean hookWithAPI() {
        manager.getPlugin().getLogger().info("The plugin will be hooked with AureliumAPI to provide Fighting XP");
        return true;
    }

    @Override
    public void addXPForPlayer(@NotNull Player player, int xp) {
        // TODO Change to API if possible after he fixes the API
        // AureliumAPI.addXp(player,Skills.FIGHTING,xp);
        manager.getPlugin().getServer().dispatchCommand(manager.getPlugin().getServer().getConsoleSender(),String.format(COMMAND_FXP_ADD,player.getName(),xp));
    }

    @Override
    public void removeXPForPlayer(@NotNull Player player, int xp) {
        manager.getPlugin().getServer().dispatchCommand(manager.getPlugin().getServer().getConsoleSender(),String.format(COMMAND_FXP_REMOVE,player.getName(),xp));
        /*
        final double currentXP = AureliumAPI.getXp(player,Skills.FIGHTING);
        final double amountToSet = (currentXP - xp);
        if(amountToSet >= 0){
            // TODO Change to API if possible after he fixes the API
            //AureliumAPI.getPlugin().getLeveler().setXp(player,Skills.FIGHTING,amountToSet);
        }else {
            // TODO Change to API if possible after he fixes the API
            //AureliumAPI.getPlugin().getLeveler().setXp(player, Skills.FIGHTING, 0);
            manager.getPlugin().getServer().dispatchCommand(manager.getPlugin().getServer().getConsoleSender(),String.format(COMMAND_FXP_REMOVE,player.getName(),xp));
         */
    }

}
