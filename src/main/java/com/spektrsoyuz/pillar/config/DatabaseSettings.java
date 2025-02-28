/*
 * Pillar
 * Created by SpektrSoyuz
 * All Rights Reserved
 */
package com.spektrsoyuz.pillar.config;

import lombok.Getter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@Getter
@ConfigSerializable
public final class DatabaseSettings {
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
}
