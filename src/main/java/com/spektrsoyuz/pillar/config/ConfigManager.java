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
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.slf4j.LoggerFactory;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class ConfigManager {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ConfigManager.class);
    private final PillarPlugin plugin;
    private final File dataFolder;
    private final Logger logger;
    private final MiniMessage mm;
    private final Map<String, CommentedConfigurationNode> configs;
    private String locale;

    // Constructor
    public ConfigManager(final PillarPlugin plugin) {
        this.plugin = plugin;
        this.dataFolder = plugin.getDataFolder();
        this.logger = plugin.getLogger();
        this.mm = MiniMessage.miniMessage();
        this.configs = new HashMap<>();

        loadConfigs();
    }

    // Method to load all config files in the resources folder
    public void loadConfigs() {
        loadConfig("config", "config.conf");

        final File localeFolder = new File(dataFolder, "locale");
        if (!localeFolder.exists()) {
            if (!localeFolder.mkdirs()) {
                logger.warning("Failed to create locale folder");
                return;
            }
        }

        final List<String> providedLocales = List.of("locale/en_US.conf");

        for (final String locale : providedLocales) {
            final File localeFile = new File(dataFolder, locale);
            if (!localeFile.exists()) {
                plugin.saveResource(locale, false);
                logger.info("Created locale file: " + localeFile);
            }
        }

        final File[] localeFiles = localeFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".conf"));
        if (localeFiles != null) {
            for (final File localeFile : localeFiles) {
                loadConfig(localeFile.getName().split("\\.")[0], "locale/" + localeFile.getName());
            }
        }

        final ConfigurationNode node = configs.get("config").node("locale");
        this.locale = node.getString("locale");
    }

    // Method to load a config file into the configs map
    private void loadConfig(final String name, final String path) {
        final File configFile = new File(dataFolder, path);
        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder().path(configFile.toPath()).build();

        if (!configFile.exists()) {
            plugin.saveResource(path, false); // save config file if it does not exist
        }

        final CommentedConfigurationNode root;
        try {
            root = loader.load();
        } catch (IOException e) {
            logger.warning("Failed to load config file " + path + ": " + e.getMessage());
            return;
        }

        configs.put(name, root);
    }

    // Method to get a message from the locale config
    public Component getMessage(final String key, final TagResolver... resolvers) {
        ConfigurationNode node = configs.get(locale).node("messages", key);
        String message = node.getString();

        if (message == null) {
            return Component.text(key);
        }

        return mm.deserialize(message, resolvers);
    }

    // Method to get the database settings from the primary config
    public DatabaseSettings getDatabaseSettings() {
        final ConfigurationNode node = configs.get("config").node("database");
        try {
            return node.get(DatabaseSettings.class);
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to get the redis settings from the primary config
    public RedisSettings getRedisSettings() {
        final ConfigurationNode node = configs.get("config").node("redis");
        try {
            return node.get(RedisSettings.class);
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to get the social settings from the primary config
    public SocialSettings getSocialSettings() {
        final ConfigurationNode node = configs.get("config").node("social");
        try {
            return node.get(SocialSettings.class);
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }

    public TagResolver papiTag(final Player player) {
        return TagResolver.resolver("papi", (args, context) -> {
            final String placeholder = args.popOr("papi tag requires an argument").value();
            final String parsed = PlaceholderAPI.setPlaceholders(player, '%' + placeholder + '%');
            return Tag.selfClosingInserting(Component.text(parsed));
        });
    }
}
