package edu.yale.library.fileservice;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import com.google.common.collect.Multimap;

import static org.slf4j.LoggerFactory.getLogger;

public class DBManager {

    private final static Logger logger = getLogger(DBManager.class);

    private static Connection conn; //TODO check

    private String path = PropsUtil.getProperty("PATH");

    public List<String> get(final String fileName) {
        if (!valid(fileName)) {
            logger.error("Invalid filename requested:{}", fileName);
            return Collections.emptyList();
        }

        final List<String> results = new ArrayList<>();
        try {
            if (!getConnection()) {
                return Collections.emptyList();
            }

            final Statement stmt = conn.createStatement();
            logger.debug("Searching for:{}", fileName);

            String sql = "select path from FILES where identifier=?";

            PreparedStatement sqlState = conn.prepareStatement(sql);
            sqlState.setString(1, fileName);

            final ResultSet rs = sqlState.executeQuery();

            while (rs.next()) {
                results.add(rs.getString(1)); //TODO check
            }

            stmt.close();
        } catch (SQLException e) {
            logger.error("Error", e);
        } finally {
            closeConnection();
        }

        return results;
    }

    public List<String> getAll() {
        final List<String> results = new ArrayList<>();
        try {
            if (!getConnection()) {
                logger.debug("Unable to get connection");
                return Collections.emptyList();
            }

            final Statement stmt = conn.createStatement();

            final ResultSet rs = stmt.executeQuery("select identifier, path from FILES"); //TODO

            while (rs.next()) {
                results.add(rs.getString(1) + ":" + rs.getString(2)); //TODO check
            }

            stmt.close();
        } catch (SQLException e) {
            logger.error("Error", e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("Error", e);
            }
        }

        return results;
    }

    public synchronized void init() throws Exception {
        if (!getConnection()) {
            logger.debug("Unable to get connection");
            return;
        }

        try (final Statement stmt = conn.createStatement()){
            final String createTable = "CREATE TABLE FILES (identifier VARCHAR(255) not NULL, path VARCHAR(500))"; //TODO len
            stmt.executeUpdate(createTable);
            logger.debug("Created table in given database...");
        } catch (SQLException e) {
            logger.error("Error", e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("Error", e);
            }
        }
    }

    public synchronized void insert() throws Exception {
        if (!tableExists()) {
            logger.debug("Initializing table");
            init();
        }

        if (!getConnection()) {
            logger.debug("Unable to get connection");
            return;
        }

        logger.debug("Inserting records into the table...");

        final FileCrawler fileCrawler = new FileCrawler(path);
        final Multimap<String, String> map = fileCrawler.getIndex();

        if (map.size() == 0) {
            return;
        }

        final Set<String> keys = map.keySet();

        if (conn.isClosed()) {
            return;
        }

        try (final Statement stmt = conn.createStatement()) {

            for (final String key : keys) {
                final List<String> path = new ArrayList<>(map.get(key));
                for (String s : path) {
                    final String id = extractFileName(key); //TODO multiple dots (?), only .tifs, and what if no numbers

                    try {
                        logger.debug("Inserting id:{} path:{}", id, s);
                        final String sql = "INSERT INTO FILES VALUES ('" + id + "', '" + s + "')";
                        stmt.executeUpdate(sql);   //TODO batch insert check
                    } catch (Exception e) {
                        logger.error("Error inserting:{}", s, e);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error", e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("Error", e);
            }
        }
    }

    public boolean tableExists() {
        if (!getConnection()) {
            logger.debug("Unable to get connection");
            return false;
        }

        try (final Statement stmt2 = conn.createStatement(); final ResultSet rs = stmt2.executeQuery("select count(*) from FILES");) {
            while (rs.next()) {
                int count = rs.getInt(1);

                if (count >= 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            logger.error("Error", e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("Error", e);
            }
        }

        return false;
    }

    // e.g., instead of /tmp/a.tif store a as the key
    public String extractFileName(String f) {
        String noExtension = FilenameUtils.removeExtension(f);
        //String fileName = noExtension.replace(path, "").replaceAll(File.separator, "").trim();
        return noExtension;
    }

    public boolean getConnection() {
        try {
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection("jdbc:h2:" + PropsUtil.getProperty("DB_PATH"), "sa", "");
        } catch (Exception e) {
            logger.error("Error getting db connection", e);
            return false;
        }
        return true;
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            logger.error("Error closing db connection", e);
        }
    }

    private boolean valid(String s) {
        return s.matches("^[0-9]+$"); // only numbers allowed //TODO might have to accommodate others
    }
}

