package com.semivanilla.bounties.reward.core;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface XPImpl {

    boolean hookWithAPI();

    void addXPForPlayer(@NotNull Player player, int xp);

    void removeXPForPlayer(@NotNull Player player, int xp);

}
