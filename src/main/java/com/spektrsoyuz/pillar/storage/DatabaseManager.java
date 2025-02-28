package com.spektrsoyuz.pillar.storage;

import com.spektrsoyuz.pillar.PillarPlugin;
import com.spektrsoyuz.pillar.config.ConfigManager;
import com.spektrsoyuz.pillar.config.DatabaseSettings;
import com.spektrsoyuz.pillar.player.PillarPlayer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public final class DatabaseManager {

    private final Logger logger;
    private final File dataFolder;
    private final ConfigManager config;
    private HikariDataSource dataSource;
    private String playersTable;

    // Constructor
    public DatabaseManager(final PillarPlugin plugin) {
        this.logger = plugin.getLogger();
        this.dataFolder = plugin.getDataFolder();
        this.config = plugin.getConfigManager();
    }

    // Create HikariCP data source (connection to SQL database)
    public void init() {
        final DatabaseSettings settings = config.getDatabaseSettings();
        final HikariConfig hikariConfig = new HikariConfig();
        final boolean isMySQL = settings.getType().equalsIgnoreCase("mysql");

        playersTable = settings.getTablePrefix() + "_players";

        if (isMySQL) {
            hikariConfig.setJdbcUrl("jdbc:mysql://" + settings.getHost() + ":" + settings.getPort() + "/" + settings.getDatabase());
            hikariConfig.setUsername(settings.getUsername());
            hikariConfig.setPassword(settings.getPassword());
            hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
            hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
            hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        } else {
            hikariConfig.setJdbcUrl("jdbc:sqlite:" + dataFolder.getAbsolutePath() + "/storage.db");
            hikariConfig.setDriverClassName("org.sqlite.JDBC");
        }

        hikariConfig.setMaximumPoolSize(10);
        dataSource = new HikariDataSource(hikariConfig);
    }

    // Create database tables if they do not exist
    public void createTables() {
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            statement.addBatch("CREATE TABLE IF NOT EXISTS " + playersTable + " (id VARCHAR(36) PRIMARY KEY, username VARCHAR(16), location VARCHAR(32));");
            statement.executeBatch();
        } catch (SQLException e) {
            logger.severe("Error creating tables: " + e.getMessage());
        }
    }

    // Asynchronous method to save a PillarPlayer to the database
    public void savePillarPlayer(PillarPlayer pillarPlayer) {

    }

    // Close the data source
    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    // Retrieves a database connection from the data source
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
