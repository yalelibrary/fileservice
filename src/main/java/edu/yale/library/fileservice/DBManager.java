package edu.yale.library.fileservice;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.google.common.collect.Multimap;

import static org.slf4j.LoggerFactory.getLogger;

public class DBManager {

    private final static Logger logger = getLogger(DBManager.class);

    //TODO externalize
    public static final String PATH = "\\\\storage.yale.edu\\home\\fc_Beinecke-807001-YUL\\DL images\\IMAGES-ARCHIVE\\Romanov TIFFS";

    private static Connection conn; //TODO

    static boolean INIT = false; //TODO

    public List<String> get(String fileName) {
        final List<String> results = new ArrayList<String>();
        try {
            getConnection();
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
            getConnection();
            final Statement stmt = conn.createStatement();
            final ResultSet rs = stmt.executeQuery("select identifier, path from FILES"); //TODO

            while (rs.next()) {
                results.add(rs.getString(1) + ":" + rs.getString(2)); //TODO check
            }

            stmt.close();
        } catch (SQLException e) {
            logger.error("Error", e);
        } finally {
            closeConnection();
        }

        return results;
    }

    public void insert()throws Exception {

        if (INIT) {
            return;
        }

        getConnection();
        final Statement stmt = conn.createStatement();

        try {
            final String createTable = "CREATE TABLE FILES (identifier VARCHAR(255) not NULL, path VARCHAR(500))"; //TODO len
            stmt.executeUpdate(createTable);
            logger.debug("Created table in given database...");
            stmt.close();
        } catch (SQLException e) {
            logger.error("Error", e);
        }

        logger.debug("Inserting records into the table...");

        final Crawler crawler = new Crawler();
        //final Multimap<String, String> map = crawler.doIndex("D:\\nikita");
        final Multimap<String, String> map =
                crawler.doIndex(PATH);

        logger.debug("Map is:{0}", map.toString());

        final Set<String> keys = map.keySet();

        final Statement stmt2 = conn.createStatement();

        for (final String key : keys) {
            final List<String> path = new ArrayList<String>(map.get(key));
            for (String s : path) {
                final String id = stripExtension(s); //TODO multiple dots (?), only .tifs, and what if no numbers

                try {
                    final String sql = "INSERT INTO FILES VALUES (" + id + ", '" + s +"')";
                    stmt2.executeUpdate(sql);   //TODO batch insert check
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }

        //"INSERT INTO FILES VALUES (123456789, 'D:\\nikita\\123456789.txt')";

        final ResultSet rs = stmt2.executeQuery("select count(*) from FILES");

        while (rs.next()) {
            logger.debug("Insert count:{}", rs.getInt(1));
        }

        stmt2.close();
        INIT = true;
        conn.close();
    }

    public String stripExtension(String f) {
        return FilenameUtils.removeExtension(f);
    }

    public void getConnection() {
        try {
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection("jdbc:h2:D:/file_service/test", "sa", ""); //TODO db name, etc.
        } catch (Exception e) {
            logger.error("Error getting connection", e);
        }
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            logger.error("Error closing connection", e);
        }
    }
}

