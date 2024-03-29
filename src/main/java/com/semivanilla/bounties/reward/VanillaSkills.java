package com.semivanilla.bounties.reward;

import com.semivanilla.bounties.hook.HookManager;
import com.semivanilla.bounties.reward.core.XPImpl;
import com.semivanilla.bounties.utils.modules.ExperienceUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class VanillaSkills implements XPImpl {

    private final HookManager manager;

    public VanillaSkills(HookManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean hookWithAPI() {
        manager.getPlugin().getLogger().info("The plugin will be hooked with providing Default Vanilla XP");
        return true;
    }

    @Override
    public void addXPForPlayer(@NotNull Player player, int xp) {
        player.giveExp(xp);
    }

    @Override
    public void removeXPForPlayer(@NotNull Player player, int xp) {
        final double xpAfterReduce = ExperienceUtils.getExp(player) - xp;
        if(xpAfterReduce > 0){
            ExperienceUtils.changeExp(player, (int) xpAfterReduce);
        }else ExperienceUtils.changeExp(player,0);
    }
}
