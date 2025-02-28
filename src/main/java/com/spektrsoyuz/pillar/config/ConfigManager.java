/*
 * Pillar
 * Created by SpektrSoyuz
 * All Rights Reserved
 */
package com.spektrsoyuz.pillar.config;

import com.spektrsoyuz.pillar.PillarPlugin;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public final class ConfigManager {

    private final PillarPlugin plugin;
    private final File dataFolder;
    private final Logger logger;
    private final MiniMessage mm;
    private CommentedConfigurationNode root;
    private CommentedConfigurationNode messages;

    // Constructor
    public ConfigManager(final PillarPlugin plugin) {
        this.plugin = plugin;
        this.dataFolder = plugin.getDataFolder();
        this.logger = plugin.getLogger();
        this.mm = MiniMessage.miniMessage();

        loadConfigs();
    }

    // Method to load all config files in the resources folder
    public void loadConfigs() {
        root = loadConfig("config.conf");
        messages = loadConfig("messages.conf");
    }

    // Method to load a config file into the configs map
    private CommentedConfigurationNode loadConfig(final String path) {
        final File configFile = new File(dataFolder, path);
        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder().path(configFile.toPath()).build();

        if (!configFile.exists()) {
            plugin.saveResource(path, false); // save config file if it does not exist
        }

        try {
            return loader.load();
        } catch (IOException e) {
            logger.warning("Failed to load config file " + path + ": " + e.getMessage());
            return null;
        }
    }

    // Method to get a message from the config
    public Component getMessage(final String key, final ConfigPlaceholder... placeholders) {
        ConfigurationNode node = messages.node("messages", key);
        String message = node.getString();

        if (message != null) {
            for (final ConfigPlaceholder placeholder : placeholders) {
                message = message.replace("{" + placeholder.name() + "}", placeholder.value());
            }
            return mm.deserialize(message);
        } else {
            return Component.text(key);
        }
    }

    // Method to get a message from the config
    public Component getMessage(final Player player, final String key, final ConfigPlaceholder... placeholders) {
        ConfigurationNode node = messages.node("messages", key);
        String message = node.getString();

        if (message != null) {
            for (final ConfigPlaceholder placeholder : placeholders) {
                message = message.replaceAll("{" + placeholder.name() + "}", placeholder.value());
            }
            return mm.deserialize(PlaceholderAPI.setPlaceholders(player, message));
        } else {
            return Component.text(key);
        }
    }

    // Method to get the database settings from the primary config
    public DatabaseSettings getDatabaseSettings() {
        final ConfigurationNode node = root.node("database");
        try {
            return node.get(DatabaseSettings.class);
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to get the redis settings from the primary config
    public RedisSettings getRedisSettings() {
        final ConfigurationNode node = root.node("redis");
        try {
            return node.get(RedisSettings.class);
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to get the social settings from the primary config
    public SocialSettings getSocialSettings() {
        final ConfigurationNode node = root.node("social");
        try {
            return node.get(SocialSettings.class);
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }
}
