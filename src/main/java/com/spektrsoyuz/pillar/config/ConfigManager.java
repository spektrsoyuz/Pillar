/*
 * Pillar
 * Created by SpektrSoyuz
 * All Rights Reserved
 */
package com.spektrsoyuz.pillar.config;

import com.spektrsoyuz.pillar.PillarPlugin;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ConfigManager {

    private final PillarPlugin plugin;
    private final File dataFolder;
    private final Logger logger;
    private final Map<String, CommentedConfigurationNode> configs;

    // Constructor
    public ConfigManager(final PillarPlugin plugin) {
        this.plugin = plugin;
        this.dataFolder = plugin.getDataFolder();
        this.logger = plugin.getLogger();
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
                loadConfig(localeFile.getName(), "locale/" + localeFile.getName());
            }
        }
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
}
