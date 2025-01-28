/*
 * Pillar
 * Created by SpektrSoyuz
 * All Rights Reserved
 */
package com.spektrsoyuz.pillar;

import com.spektrsoyuz.pillar.command.server.*;
import com.spektrsoyuz.pillar.command.social.*;
import com.spektrsoyuz.pillar.config.ConfigManager;
import com.spektrsoyuz.pillar.config.SocialSettings;
import com.spektrsoyuz.pillar.tpa.TPAManager;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings({"UnstableApiUsage"})
public final class PillarPlugin extends JavaPlugin {

    private ConfigManager configManager;
    private TPAManager tpaManager;

    @Override
    public void onLoad() {
        // Plugin load logic
        configManager = new ConfigManager(this);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        tpaManager = new TPAManager(this);

        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public TPAManager getTPAManager() {
        return tpaManager;
    }

    private void registerCommands() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            final Commands registrar = commands.registrar();

            new BroadcastCommand(this, registrar);

            socialCommands(registrar);
        });
    }

    private void socialCommands(final Commands registrar) {
        final SocialSettings social = configManager.getSocialSettings();

        if (social.isDiscord()) {
            new DiscordCommand(this, registrar);
        }
        if (social.isForums()) {
            new ForumsCommand(this, registrar);
        }
        if (social.isReddit()) {
            new RedditCommand(this, registrar);
        }
        if (social.isTwitch()) {
            new TwitchCommand(this, registrar);
        }
        if (social.isTwitter()) {
            new TwitterCommand(this, registrar);
        }
        if (social.isWebsite()) {
            new WebsiteCommand(this, registrar);
        }
        if (social.isYoutube()) {
            new YoutubeCommand(this, registrar);
        }
    }

    private void registerListeners() {

    }
}
