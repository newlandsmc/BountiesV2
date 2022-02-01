package com.semivanilla.bounties.hook.skills.core;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface SkillImpl {

    default boolean hookWithAPI() {
        return true;
    }

    void addXPForPlayer(@NotNull Player player, int xp);

    void removeXPForPlayer(@NotNull Player player, int xp);

}
