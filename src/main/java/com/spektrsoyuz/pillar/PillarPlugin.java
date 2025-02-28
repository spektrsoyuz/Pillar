/*
 * Pillar
 * Created by SpektrSoyuz
 * All Rights Reserved
 */
package com.spektrsoyuz.pillar;

import com.spektrsoyuz.pillar.command.player.*;
import com.spektrsoyuz.pillar.command.server.*;
import com.spektrsoyuz.pillar.command.social.*;
import com.spektrsoyuz.pillar.config.ConfigManager;
import com.spektrsoyuz.pillar.config.SocialSettings;
import com.spektrsoyuz.pillar.listener.PlayerListener;
import com.spektrsoyuz.pillar.player.PillarPlayerManager;
import com.spektrsoyuz.pillar.storage.DatabaseManager;
import com.spektrsoyuz.pillar.tpa.TPAManager;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@SuppressWarnings({"UnstableApiUsage"})
public final class PillarPlugin extends JavaPlugin {

    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private PillarPlayerManager pillarPlayerManager;
    private TPAManager tpaManager;

    @Override
    public void onLoad() {
        // Plugin load logic
        configManager = new ConfigManager(this);

        int configVersion = configManager.getConfigVersion();
        if (configVersion != PillarUtils.CONFIG_VERSION) {
            getLogger().severe(String.format("Config version (" + configVersion + ") does not match expected version (" + PillarUtils.CONFIG_VERSION + "). Proceed with caution!"));
        }
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        databaseManager = new DatabaseManager(this);
        databaseManager.init();
        databaseManager.createTables();
        pillarPlayerManager = new PillarPlayerManager(this);
        tpaManager = new TPAManager(this);

        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        pillarPlayerManager.saveAll();

        if (databaseManager != null) {
            databaseManager.close();
        }
    }

    private void registerCommands() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            final Commands registrar = commands.registrar();

            new BackCommand(this, registrar);
            new GamemodeCommand(this, registrar);
            new GMACommand(this, registrar);
            new GMCCommand(this, registrar);
            new GMSCommand(this, registrar);
            new GMSPCommand(this, registrar);
            new PingCommand(this, registrar);
            new BroadcastCommand(this, registrar);

            socialCommands(registrar);
        });
    }

    private void registerListeners() {
        new PlayerListener(this);
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
}
