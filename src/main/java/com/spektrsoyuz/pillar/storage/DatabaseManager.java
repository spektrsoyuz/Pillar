/*
 * Pillar
 * Created by SpektrSoyuz
 * All Rights Reserved
 */
package com.spektrsoyuz.pillar.storage;

import com.spektrsoyuz.pillar.PillarPlugin;
import com.spektrsoyuz.pillar.config.ConfigManager;
import com.spektrsoyuz.pillar.config.DatabaseSettings;
import com.spektrsoyuz.pillar.player.PillarPlayer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Location;

import java.io.File;
import java.sql.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
            final Statement statement = connection.createStatement();
            statement.addBatch("CREATE TABLE IF NOT EXISTS " + playersTable + " (id VARCHAR(36) PRIMARY KEY, username VARCHAR(16), back_location VARCHAR(32));");
            statement.executeBatch();
        } catch (SQLException ex) {
            logger.severe("Error creating tables: " + ex.getMessage());
        }
    }

    // Asynchronous method to save a PillarPlayer to the database
    public void savePillarPlayer(final PillarPlayer pillarPlayer) {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection()) {
                final String sql = "INSERT INTO " + playersTable + " (id, username, back_location) VALUES (?, ?, ?)" +
                        " ON DUPLICATE KEY UPDATE username = ?, back_location = ?;";
                final PreparedStatement statement = connection.prepareStatement(sql);

                // Insert values if missing
                statement.setString(1, pillarPlayer.getMojangId().toString());
                statement.setString(2, pillarPlayer.getUsername());
                statement.setString(3, serializeLocation(pillarPlayer.getBackLocation()));

                // Update existing values
                statement.setString(4, pillarPlayer.getUsername());
                statement.setString(5, serializeLocation(pillarPlayer.getBackLocation()));
            } catch (SQLException ex) {
                logger.severe("Error saving pillar player: " + ex.getMessage());
            }
        });
    }

    // Asynchronous method to get a PillarPlayer by UUID from the database
    public CompletableFuture<PillarPlayer> queryPillarPlayer(final UUID mojangId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection()) {
                final PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + playersTable + " WHERE id = ?");
                statement.setString(1, mojangId.toString());

                final ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return getPlayerResult(resultSet);
                }
            } catch (SQLException e) {
                logger.severe("Error querying pillar player by UUID: " + e.getMessage());
            }
            return null;
        });
    }

    // Asynchronous method to get a PillarPlayer by username from the database
    public CompletableFuture<PillarPlayer> queryPillarPlayer(final String username) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection()) {
                final PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + playersTable + " WHERE username = ?");
                statement.setString(1, username);

                final ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return getPlayerResult(resultSet);
                }
            } catch (SQLException e) {
                logger.severe("Error querying pillar player by username: " + e.getMessage());
            }
            return null;
        });
    }

    // Asynchronous method to get all PillarPlayer results from the database
    public CompletableFuture<List<PillarPlayer>> queryPillarPlayers() {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection()) {
                final List<PillarPlayer> pillarPlayers = new ArrayList<>();
                final PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + playersTable + ";");

                final ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    pillarPlayers.add(getPlayerResult(resultSet));
                }
                return pillarPlayers;
            } catch (SQLException e) {
                logger.severe("Error querying all pillar players: " + e.getMessage());
            }
            return null;
        });
    }

    // Get a PillarPlayer from a SQL result set
    private PillarPlayer getPlayerResult(final ResultSet resultSet) throws SQLException {
        final UUID mojangId = UUID.fromString(resultSet.getString("id"));
        final String username = resultSet.getString("username");
        final Location backLocation = deserializeLocation(resultSet.getString("back_location"));

        return new PillarPlayer(mojangId, username, backLocation);
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

    // Deserialize a Bukkit Location into a database-compatible string
    private Location deserializeLocation(final String locationString) {
        final String[] stringArgs = locationString.split(",");
        final Map<String, Object> locationMap = new HashMap<>();

        locationMap.put("world", stringArgs[0]);
        locationMap.put("x", stringArgs[1]);
        locationMap.put("y", stringArgs[2]);
        locationMap.put("z", stringArgs[3]);
        locationMap.put("yaw", stringArgs[4]);
        locationMap.put("pitch", stringArgs[5]);

        return Location.deserialize(locationMap);
    }

    // Serialize a database string into a Bukkit Location
    private String serializeLocation(final Location location) {
        final Map<String, Object> locationMap = location.serialize();
        return locationMap.values().stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }
}
