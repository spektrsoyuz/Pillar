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
public final class SocialSettings {

    private final boolean discord;
    private final boolean forums;
    private final boolean reddit;
    private final boolean twitch;
    private final boolean twitter;
    private final boolean website;
    private final boolean youtube;

    // Constructor
    public SocialSettings() {
        this.discord = false;
        this.forums = false;
        this.reddit = false;
        this.twitch = false;
        this.twitter = false;
        this.website = false;
        this.youtube = false;
    }
}
