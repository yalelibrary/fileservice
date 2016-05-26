package edu.yale.library.fileservice;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
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

    //TODO externalize
    public static final String PATH = "\\\\storage.yale.edu\\home\\fc_Beinecke-807001-YUL\\DL images\\IMAGES-ARCHIVE\\Romanov TIFFS";

    private static Connection conn; //TODO check

    public List<String> get(String fileName) {
        final List<String> results = new ArrayList<String>();
        try {
            if (!getConnection()) {
                return Collections.emptyList();
            }

            final Statement stmt = conn.createStatement();

            final ResultSet rs = stmt.executeQuery("select path from FILES where identifier=" + fileName); //TODO

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
        final List<String> results = new ArrayList<String>();
        try {
            if (!getConnection()) {
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

    public void init() throws Exception {
        if (!getConnection()) {
            return;
        }

        try (final Statement stmt = conn.createStatement();){
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

    public void insert() throws Exception {

        if (!tableExists()) {
            logger.debug("Initializing table");
            init();
        }

        if (!getConnection()) {
            return;
        }

        logger.debug("Inserting records into the table...");

        final FileCrawler fileCrawler = new FileCrawler();

        final Multimap<String, String> map = fileCrawler.getIndex(PATH);

        final Set<String> keys = map.keySet();


        try (final Statement stmt = conn.createStatement()) {

            for (final String key : keys) {
                final List<String> path = new ArrayList<>(map.get(key));
                for (String s : path) {
                    final String id = stripExtension(s); //TODO multiple dots (?), only .tifs, and what if no numbers

                    try {
                        final String sql = "INSERT INTO FILES VALUES (" + id + ", '" + s + "')";
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

    public String stripExtension(String f) {
        return FilenameUtils.removeExtension(f);
    }

    public boolean getConnection() {
        try {
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection("jdbc:h2:D:/file_service/test", "sa", ""); //TODO db name, etc.
        } catch (Exception e) {
            logger.error("Error getting connection", e);
            return false;
        }
        return true;
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            logger.error("Error closing connection", e);
        }
    }
}

