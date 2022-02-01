package com.semivanilla.bounties.model;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Bounty {

    private final UUID playerUUID;
    private long remainingTime;
    private int killed;

    public Bounty(UUID playerUUID, long remainingTime, int killed) {
        this.playerUUID = playerUUID;
        this.remainingTime = remainingTime;
        this.killed = killed;
    }

    public Bounty(UUID playerUUID, long remainingTime) {
        this.playerUUID = playerUUID;
        this.remainingTime = remainingTime;
        this.killed = 0;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    public int getKilled() {
        return killed;
    }

    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }

    public void setKilled(int killed) {
        this.killed = killed;
    }

    public String getRemaining(){
        return System.currentTimeMillis() > this.remainingTime ? String.valueOf(System.currentTimeMillis()-remainingTime) : "0";
    }

    public String getFormattedRemaining(){
        String returnTime = "0";
        final long time = this.remainingTime - System.currentTimeMillis();
        final int hr = (int) TimeUnit.MILLISECONDS.toHours(time);

        if(hr >= 1){
            returnTime = hr+" Hours";
        }else {
            final int mins = (int) TimeUnit.MILLISECONDS.toMinutes(time);
            if(mins > 1)
                returnTime = mins+" Minutes";
            else return "Less than a minute";
        }

        return returnTime;
    }

    public Optional<Player> getPlayer(){
        return Optional.ofNullable(Bukkit.getPlayer(this.playerUUID));
    }

    public boolean isPlayerOnline(){
        return Bukkit.getServer().getPlayer(this.playerUUID) != null;
    }

    public void addKill(long updateTime){
        this.killed +=1;
        this.remainingTime = updateTime;
    }

}
