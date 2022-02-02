package com.semivanilla.bounties.model;

import java.util.UUID;

public final class PlayerStatistics {

    private final UUID playerID;
    private int bountyKills;
    private int kills;
    private int deaths;

    public PlayerStatistics(UUID playerID) {
        this.playerID = playerID;
        this.deaths = 0;
        this.kills = 0;
        this.bountyKills = 0;
    }

    public PlayerStatistics(UUID playerID, int bountyKills, int kills, int deaths) {
        this.playerID = playerID;
        this.bountyKills = bountyKills;
        this.kills = kills;
        this.deaths = deaths;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public int getBountyKills() {
        return bountyKills;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setBountyKills(int bountyKills) {
        this.bountyKills = bountyKills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void addKill(){
        this.kills++;
    }

    public void addBountyKills(){
        this.kills++;
        this.bountyKills++;
    }

    public void addDeath(){
        this.deaths++;
    }

    public float getKDRatio(){
        return kills/deaths;
    }
}
