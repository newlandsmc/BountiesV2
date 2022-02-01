package com.semivanilla.bounties.utils.modules;

import com.semivanilla.bounties.utils.UtilityManager;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.List;

import static com.semivanilla.bounties.utils.modules.MessageFormatter.transform;

public class MessagingUtils {

    private final UtilityManager manager;
    private final BukkitAudiences audiences;

    public MessagingUtils(UtilityManager manager) {
        this.manager = manager;
        this.audiences = BukkitAudiences.create(manager.getPlugin());
    }

    public void sendTo(@NotNull final Player player, String message){
        audiences.player(player).sendMessage(transform(message));
    }

    public void sendTo(@NotNull final Player player, String message, InternalPlaceholders... placeholders){
        audiences.player(player).sendMessage(transform(message,placeholders));
    }

    public void sendTo(@NotNull final Player player, List<String> messages){
        messages.forEach(m -> {
            this.sendTo(player,m);
        });
    }

    public void sendTo(@NotNull final Player player, List<String> messages, InternalPlaceholders... placeholders){
        messages.forEach(m -> {
            this.sendTo(player,m,placeholders);
        });
    }

    public void sendToAsComponent(@NotNull final Player player, Component message){
        audiences.player(player).sendMessage(message);
    }

    public void sendToAsComponent(@NotNull final Player player, List<Component> message){
        message.forEach(m -> {
            sendToAsComponent(player,m);
        });
    }

    public void sendActionBar(@NotNull Player player, String message){
        audiences.player(player).sendActionBar(transform(message));
    }

    public void sendActionBar(@NotNull List<Player> players, String message){
        players.forEach((p) -> sendActionBar(p,message));
    }

    public void setBossBar(@NotNull Player player,@NotNull String barString, float percent , BossBar.Color color, BossBar.Overlay style, int duration){
        final BossBar bossBar = BossBar.bossBar(transform(barString),percent, color,style);
        audiences.player(player).showBossBar(bossBar);
        manager.getPlugin().getServer().getScheduler().runTaskLater(manager.getPlugin(), new Runnable() {
            @Override
            public void run() {
                audiences.player(player).hideBossBar(bossBar);
            }
        }, 20L *duration);
    }

    public  BossBar setBossBar(@NotNull Player player,@NotNull String barString,float percent ,BossBar.Color color, BossBar.Overlay style){
        final BossBar bossBar = BossBar.bossBar(transform(barString),percent, color,style);
        audiences.player(player).showBossBar(bossBar);
        return bossBar;
    }

    public void sendTitle(@NotNull Player player, String header, String footer ){
        final Title title = Title.title(
                transform(header)
                ,transform(footer)
        );

        audiences.player(player).showTitle(title);
    }

    public void sendTitle(@NotNull Player player, String header, String footer, long fadeIn, long stay, long fadeOut){
        final Title.Times timeObj = Title.Times.times(Duration.ofSeconds(fadeIn),Duration.ofSeconds(stay),Duration.ofSeconds(fadeOut));
        final Title title = Title.title(
                transform(header)
                ,transform(footer)
                ,timeObj
        );

        audiences.player(player).showTitle(title);
    }

    public void sendTitle(@NotNull Player player, String header){
        final Title title = Title.title(
                transform(header),
                Component.empty()
        );

        audiences.player(player).showTitle(title);
    }

    public void sendTitle(@NotNull Player player, String header, long fadeIn, long stay, long fadeOut){
        final Title.Times timeObj = Title.Times.times(Duration.ofSeconds(fadeIn),Duration.ofSeconds(stay),Duration.ofSeconds(fadeOut));
        final Title title = Title.title(
                transform(header)
                ,Component.empty()
                ,timeObj
        );

        audiences.player(player).showTitle(title);
    }

    public void broadcast(String message){
        if(StringUtils.isBlank(message))
            return;
        audiences.all().sendMessage(transform(message));
    }
}
