package com.semivanilla.bounties.hook.skills;

import com.archyx.aureliumskills.api.AureliumAPI;
import com.archyx.aureliumskills.skills.Skills;
import com.semivanilla.bounties.hook.HookManager;
import com.semivanilla.bounties.hook.skills.core.SkillImpl;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class AureliumSkills implements SkillImpl {

    private final HookManager manager;

    public AureliumSkills(HookManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean hookWithAPI() {
        manager.getPlugin().getLogger().info("The plugin will be hooked with AureliumAPI to provide Fighting XP");
        return true;
    }

    @Override
    public void addXPForPlayer(@NotNull Player player, int xp) {
        AureliumAPI.addXp(player,Skills.FIGHTING,xp);
    }

    @Override
    public void removeXPForPlayer(@NotNull Player player, int xp) {
        final double currentXP = AureliumAPI.getXp(player,Skills.FIGHTING);
        final double amountToSet = (currentXP - xp);
        if(amountToSet >= 0){
            AureliumAPI.getPlugin().getLeveler().setXp(player,Skills.FIGHTING,amountToSet);
        }else {
            AureliumAPI.getPlugin().getLeveler().setXp(player, Skills.FIGHTING, 0);
        }
    }
}
