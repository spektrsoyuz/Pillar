/*
 * Pillar
 * Created by SpektrSoyuz
 * All Rights Reserved
 */
package com.spektrsoyuz.pillar.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class DatabaseSettings {
    private final String type;
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private final String tablePrefix;

    // Constructor
    public DatabaseSettings() {
        this.type = "sqlite";
        this.host = "localhost";
        this.port = 3306;
        this.database = "database";
        this.username = "username";
        this.password = "";
        this.tablePrefix = "pillar";
    }

    public String getType() {
        return type;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }
}
