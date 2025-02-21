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
}
