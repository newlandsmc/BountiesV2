package com.semivanilla.bounties.storage;

import com.semivanilla.bounties.model.Bounty;
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

public class H2 extends AbstractSQL implements DataStorageImpl {

    private final DatabaseHandler databaseHandler;

    public H2(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    @Override
    public boolean initStorageConnection() {
        if(checkForClass("org.h2.Driver")){
            databaseHandler.getPlugin().getLogger().severe("JDBC Class is not present, This plugin won't be able to connect to HSQLDB.");
            return false;
        }

        final File file = new File(databaseHandler.getPlugin().getDataFolder()+File.separator+"data-storage","database.db");
        return connect("h2",file.getAbsolutePath());
    }

    @Override
    public void prepareDatabaseTables() {
        PreparedStatement ps1 = null,ps2 = null,ps3 = null;
        try {
            ps1 = sqlConnection.prepareStatement("CREATE TABLE "+BOUNTY_TABLE_NAME+" IF NOT EXISTS (`pl_id` VARCHAR(40) NOT NULL, `kills` INTEGER NOT NULL , `time` INTEGER NOT NULL );");
            //ps2 = sqlConnection.prepareStatement("CREATE TABLE "+PLAYER_DATA_TABLE_NAME+" IF NOT EXISTS (`pl_id` VARCHAR(40) NOT NULL, `bounty_kills` INTEGER NOT NULL DEFAULT  0);");
            ps3 = sqlConnection.prepareStatement("CREATE TABLE "+ XP_QUEUE_TABLE_NAME +" IF NOT EXISTS (`pl_id` VARCHAR(40) NOT NULL,`xp` INTEGER NOT NULL, `add` BOOLEAN NOT NULL);");

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
            if(!sqlConnection.isClosed())
                sqlConnection.close();
        }catch (Exception ignored){}
    }

    @Override
    public String storageType() {
        return "H2";
    }

    @Override
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

}

