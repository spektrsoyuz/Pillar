/*
 * Pillar
 * Created by SpektrSoyuz
 * All Rights Reserved
 */
package com.spektrsoyuz.pillar.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class RedisSettings {
    private final boolean enabled;
    private final String host;
    private final int port;
    private final String password;

    // Constructor
    public RedisSettings() {
        this.enabled = false;
        this.host = "localhost";
        this.port = 3306;
        this.password = "";
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPassword() {
        return password;
    }
}
