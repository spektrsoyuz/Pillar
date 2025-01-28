package com.spektrsoyuz.pillar.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class SocialSettings {

    private final boolean discord;
    private final boolean forums;
    private final boolean reddit;
    private final boolean twitch;
    private final boolean twitter;
    private final boolean website;
    private final boolean youtube;

    public SocialSettings() {
        this.discord = false;
        this.forums = false;
        this.reddit = false;
        this.twitch = false;
        this.twitter = false;
        this.website = false;
        this.youtube = false;
    }

    public boolean isDiscord() {
        return discord;
    }

    public boolean isForums() {
        return forums;
    }

    public boolean isReddit() {
        return reddit;
    }

    public boolean isTwitch() {
        return twitch;
    }

    public boolean isTwitter() {
        return twitter;
    }

    public boolean isWebsite() {
        return website;
    }

    public boolean isYoutube() {
        return youtube;
    }
}
