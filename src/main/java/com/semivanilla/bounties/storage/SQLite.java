package com.semivanilla.bounties.storage;

import com.semivanilla.bounties.enums.QueueAction;
import com.semivanilla.bounties.model.Bounty;
import com.semivanilla.bounties.model.BountyQueue;
import com.semivanilla.bounties.model.PlayerStatistics;
import com.semivanilla.bounties.storage.core.AbstractSQL;
import com.semivanilla.bounties.storage.core.DataStorageImpl;
import com.semivanilla.bounties.storage.core.DatabaseHandler;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public final class SQLite extends AbstractSQL implements DataStorageImpl {

    private final DatabaseHandler databaseHandler;

    public SQLite(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    @Override
    public boolean initStorageConnection() {
        if(!checkForClass("org.sqlite.JDBC")){
            databaseHandler.getPlugin().getLogger().severe("JDBC Class is not present, This plugin won't be able to connect to H2.");
            return false;
        }

        final File file = new File(databaseHandler.getPlugin().getDataFolder()+File.separator+"data-storage","database.db");
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();

        return connect("sqlite",file.getAbsolutePath());
    }

    @Override
    public void prepareDatabaseTables() {
        PreparedStatement ps1 = null,ps2 = null,ps3 = null;
        try {
            ps1 = sqlConnection.prepareStatement("CREATE TABLE IF NOT EXISTS "+BOUNTY_TABLE_NAME+"  (`pl_id` VARCHAR(40) NOT NULL, `kills` INTEGER NOT NULL , `time` INTEGER NOT NULL );");
            ps2 = sqlConnection.prepareStatement("CREATE TABLE IF NOT EXISTS "+PLAYER_DATA_TABLE_NAME+"  (`pl_id` VARCHAR(40) NOT NULL, `bounty_kills` INTEGER NOT NULL DEFAULT  0,`kills` INTEGER NOT NULL DEFAULT  0,`deaths` INTEGER NOT NULL DEFAULT  0);");
            ps3 = sqlConnection.prepareStatement("CREATE TABLE IF NOT EXISTS "+ XP_QUEUE_TABLE_NAME +"  (`pl_id` VARCHAR(40) NOT NULL,`xp` INTEGER NOT NULL, `action` VARCHAR(10) NOT NULL);");

            ps1.execute();
            ps2.execute();
            ps3.execute();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(ps1 != null){
                try {
                    if(!ps1.isClosed())
                        ps1.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if(ps2 != null){
                try {
                    if(!ps2.isClosed())
                        ps2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if(ps3 != null){
                try {
                    if(!ps3.isClosed())
                        ps3.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void close() {
        if(sqlConnection == null)
            return;

        try {
            if(!sqlConnection.isClosed()) {
                databaseHandler.getPlugin().getLogger().info("Starting to save data!");
                databaseHandler.getPlugin().getDataManager().getOnlineBounties().forEachRemaining(this::saveBountySync);
                databaseHandler.getPlugin().getDataManager().getStatisticsManager().getAllLoadedPlayerStats().forEachRemaining(this::savePlayerStatisticsSync);
                databaseHandler.getPlugin().getLogger().info("Datasaving has been completed...Preparing to shut down database");
                sqlConnection.close();
            }
        }catch (Exception ignored){}
    }

    @Override
    public String storageType() {
        return "H2";
    }

    @Override
    public CompletableFuture<PlayerStatistics> getOrRegister(UUID uuid) {
        return CompletableFuture.supplyAsync(new Supplier<PlayerStatistics>() {
            @Override
            public PlayerStatistics get() {
                try {
                    PreparedStatement ps = sqlConnection.prepareStatement("SELECT * FROM "+PLAYER_DATA_TABLE_NAME+" WHERE `pl_id` = ?;");
                    if(ps == null)
                        return new PlayerStatistics(uuid);

                    ps.setString(1,uuid.toString());

                    ResultSet set = ps.executeQuery();

                    if(set == null)
                        return new PlayerStatistics(uuid);

                    if(set.next()){
                        final PlayerStatistics statistics = new PlayerStatistics(uuid,set.getInt("bounty_kills"),set.getInt("kills"),set.getInt("deaths"));
                        set.close();
                        ps.close();
                        return statistics;
                    }else {
                        final PreparedStatement ps2 = sqlConnection.prepareStatement("INSERT INTO "+PLAYER_DATA_TABLE_NAME+" VALUES (?,?,?,?);");
                        ps2.setString(1,uuid.toString());
                        ps2.setInt(2,0);
                        ps2.setInt(3,0);
                        ps2.setInt(4,0);
                        ps2.executeUpdate();
                        ps2.close();

                        set.close();
                        ps.close();
                        return new PlayerStatistics(uuid);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    return new PlayerStatistics(uuid);
                }
            }
        });
    }

    /*
    public CompletableFuture<Optional<Bounty>> getIfPresent(@NotNull UUID uuid) {
        return CompletableFuture.supplyAsync(new Supplier<Optional<Bounty>>() {
            @Override
            public Optional<Bounty> get() {
                try{
                    final PreparedStatement ps = sqlConnection.prepareStatement("SELECT * FROM "+BOUNTY_TABLE_NAME+" WHERE `pl_id` = ?;");
                    if(ps == null)
                        return Optional.empty();

                    ps.setString(1,uuid.toString());

                    ResultSet set = ps.executeQuery();
                    if(set == null)
                        return Optional.empty();

                    if(set.next()){
                        final Bounty bounty = new Bounty(uuid,set.getLong("time"),set.getInt("kills"));
                        set.close();
                        ps.close();
                        return Optional.of(bounty);
                    }else {
                        set.close();
                        ps.close();
                        return Optional.empty();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    return Optional.empty();
                }
            }
        });
    }
     */

    @Override
    public void registerNewBounty(@NotNull Bounty bounty) {
        databaseHandler.getPlugin().getServer().getScheduler().runTaskAsynchronously(databaseHandler.getPlugin(), new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement ps = sqlConnection.prepareStatement("INSERT INTO "+BOUNTY_TABLE_NAME+" VALUES (?,?,?);");

                    if(ps == null)
                        return;

                    ps.setString(1,bounty.getPlayerUUID().toString());
                    ps.setInt(2,bounty.getKilled());
                    ps.setLong(3,bounty.getRemainingTime());

                    ps.executeUpdate();
                    ps.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void removeABounty(@NotNull UUID uuid) {
        databaseHandler.getPlugin().getServer().getScheduler().runTaskAsynchronously(databaseHandler.getPlugin(), new Runnable() {
            @Override
            public void run() {
                try {

                    PreparedStatement ps = sqlConnection.prepareStatement("DELETE FROM "+BOUNTY_TABLE_NAME+" WHERE `pl_id` = ?;");

                    if(ps == null)
                        return;

                    ps.setString(1,uuid.toString());
                    ps.execute();
                    ps.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public Iterator<Bounty> getAllCurrentBounties() {
        List<Bounty> bountyList = new ArrayList<>();
        try {
            PreparedStatement ps = sqlConnection.prepareStatement("SELECT * FROM "+BOUNTY_TABLE_NAME+";");
            if(ps != null){
                ResultSet set = ps.executeQuery();
                if(set != null){
                    while (set.next()){
                        final Bounty bounty =new Bounty(UUID.fromString(set.getString("pl_id")),set.getLong("time"),set.getInt("kills"));
                        bountyList.add(bounty);
                    }
                    set.close();
                }
                ps.close();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return bountyList.iterator();
    }

    @Override
    public void saveBountyAsync(@NotNull Bounty bounty) {
        databaseHandler.getPlugin().getServer().getScheduler().runTaskAsynchronously(databaseHandler.getPlugin(), new Runnable() {
            @Override
            public void run() {
               try {
                   PreparedStatement ps = sqlConnection.prepareStatement("UPDATE "+BOUNTY_TABLE_NAME+" SET `time` = ?, `kills` = ? WHERE `pl_id` = ?;");
                   if(ps == null)
                       return;

                   ps.setLong(1,bounty.getRemainingTime());
                   ps.setInt(2,bounty.getKilled());
                   ps.setString(3,bounty.getPlayerUUID().toString());


                   ps.executeUpdate();
                   ps.close();
               }catch (Exception e){
                   e.printStackTrace();
               }
            }
        });
    }

    @Override
    public void saveBountySync(@NotNull Bounty bounty) {
        try {
            PreparedStatement ps = sqlConnection.prepareStatement("UPDATE "+BOUNTY_TABLE_NAME+" SET `time` = ?, `kills` = ? WHERE `pl_id` = ?;");
            if(ps == null)
                return;

            ps.setLong(1,bounty.getRemainingTime());
            ps.setInt(2,bounty.getKilled());
            ps.setString(3,bounty.getPlayerUUID().toString());



            ps.executeUpdate();
            ps.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void savePlayerStatisticsSync(@NotNull PlayerStatistics statistics) {
        try {
            PreparedStatement ps = sqlConnection.prepareStatement("UPDATE "+PLAYER_DATA_TABLE_NAME+" SET `bounty_kills` = ?, `kills` = ?, `deaths` = ? WHERE `pl_id` = ?;");

            if(ps == null)
                return;

            ps.setInt(1,statistics.getBountyKills());
            ps.setInt(2,statistics.getKills());
            ps.setInt(3,statistics.getDeaths());
            ps.setString(4,statistics.getPlayerID().toString());


            ps.executeUpdate();
            ps.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void savePlayerStatisticsAsync(@NotNull PlayerStatistics statistics) {
        databaseHandler.getPlugin().getServer().getScheduler().runTaskAsynchronously(databaseHandler.getPlugin(), new Runnable() {
            @Override
            public void run() {
                try {

                    PreparedStatement ps = sqlConnection.prepareStatement("UPDATE "+PLAYER_DATA_TABLE_NAME+" SET `bounty_kills` = ?, `kills` = ?, `deaths` = ? WHERE `pl_id` = ?;");

                    if(ps == null)
                        return;

                    ps.setInt(1,statistics.getBountyKills());
                    ps.setInt(2,statistics.getKills());
                    ps.setInt(3,statistics.getDeaths());
                    ps.setString(4,statistics.getPlayerID().toString());


                    ps.executeUpdate();
                    ps.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void registerBountyQueue(@NotNull BountyQueue queue) {
        databaseHandler.getPlugin().getServer().getScheduler().runTaskAsynchronously(databaseHandler.getPlugin(), new Runnable() {
            @Override
            public void run() {
                try {
                    final PreparedStatement ps = sqlConnection.prepareStatement("INSERT INTO "+XP_QUEUE_TABLE_NAME+" VALUES (?,?,?);");
                    if(ps == null)
                        return;

                    ps.setString(1,queue.getUuid().toString());
                    ps.setInt(2,queue.getValueToExecute());
                    ps.setString(3,queue.getAction().name());

                    ps.executeUpdate();
                    ps.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void removeBountyQueue(@NotNull UUID uuid) {
        databaseHandler.getPlugin().getServer().getScheduler().runTaskAsynchronously(databaseHandler.getPlugin(), new Runnable() {
            @Override
            public void run() {
                try {

                    final PreparedStatement ps = sqlConnection.prepareStatement("DELETE FROM "+XP_QUEUE_TABLE_NAME+" WHERE `pl_id` = ?;");
                    if(ps == null)
                        return;

                    ps.setString(1,uuid.toString());

                    ps.executeUpdate();
                    ps.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public Iterator<BountyQueue> getAllBountyQueues() {
        final List<BountyQueue> queueList = new ArrayList<>();
        try {
            final PreparedStatement ps = sqlConnection.prepareStatement("SELECT * FROM "+XP_QUEUE_TABLE_NAME+";");
            if(ps != null){
                final ResultSet set = ps.executeQuery();
                if(set != null){
                    while (set.next()){
                        final BountyQueue bountyQueue = new BountyQueue(UUID.fromString(set.getString("pl_id")), QueueAction.valueOf(set.getString("action")),set.getInt("xp"));
                        queueList.add(bountyQueue);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return queueList.iterator();
    }

}

