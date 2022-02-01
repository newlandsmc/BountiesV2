package com.semivanilla.bounties.storage.core;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class AbstractSQL {

    protected static final String BOUNTY_TABLE_NAME = "tbl_bounty";
    protected static final String PLAYER_DATA_TABLE_NAME = "tbl_players";
    protected static final String XP_QUEUE_TABLE_NAME = "tbl_cmd_queue";

    protected static final String JDBC_REMOTE_URL = "jdbc:"
            + "%s" //Type of DB (postgresql,mysql)
            + "://"
            + "%s" // host
            + ":"
            + "%d" // port
            + "/"
            + "%s" // database
            + "?autoReconnect="
            + "%s" // auto reconnect
            + "&"
            + "useSSL="
            + "%s" // use ssl
            ;
    protected static final String JDBC_LOCAL_URL = "jdbc:" +
            "%s" + //Type of DB (sqlite, h2, hsqldb)
            "%s" // File path of the database
            ;

    protected Connection sqlConnection;

    public AbstractSQL() {
    }

    public boolean connect(@NotNull String hostType,@NotNull final String path){
        try {
            this.sqlConnection = DriverManager.getConnection(String.format(JDBC_LOCAL_URL,hostType,path));

            if(sqlConnection == null)
                return false;

            return !sqlConnection.isClosed();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean connect(@NotNull String hostType,@NotNull final String host, String username, String password, int port, String databaseName, boolean ssl, boolean reconnect){
        try {
            this.sqlConnection = DriverManager.getConnection(String.format(JDBC_LOCAL_URL,hostType,host,port,databaseName,reconnect,ssl),username,password);

            if(sqlConnection == null)
                return false;

            return !sqlConnection.isClosed();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void disconnect(){
        if(this.sqlConnection == null)
            return;

        try {
            if(this.sqlConnection.isClosed())
                return;

            sqlConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkForClass(@NotNull String className){
        try {
            Class.forName(className);
        } catch (ClassNotFoundException ex) {
            return false;
        }

        return true;
    }
}
