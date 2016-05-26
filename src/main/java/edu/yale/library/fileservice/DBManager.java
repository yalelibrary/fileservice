package edu.yale.library.fileservice;

import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class DBManager {

    private final static Logger logger = getLogger(DBManager.class);

    private static Connection conn; //TODO

    static boolean INIT = false; //TODO

    public List<String> test(String fileName) {
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

    public void insert()throws Exception {

        if (INIT) {
            return;
        }

        getConnection();
        final Statement stmt = conn.createStatement();

        try {
            final String createTable = "CREATE TABLE FILES (identifier INTEGER not NULL, path VARCHAR(255))"; //TODO
            stmt.executeUpdate(createTable);
            logger.debug("Created table in given database...");
            stmt.close();
        } catch (SQLException e) {
            logger.error("Error", e);
        }

        logger.debug("Inserting records into the table...");

        final Statement stmt2 = conn.createStatement();
        final String sql = "INSERT INTO FILES VALUES (123456789, 'D:\\nikita\\123456789.txt')";
        stmt2.executeUpdate(sql);

        logger.debug("Inserted records into the table...");

        final ResultSet rs = stmt2.executeQuery("select count(*) from FILES");

        while (rs.next()) {
            logger.debug("Insert count:{}", rs.getInt(1));
        }

        stmt2.close();
        INIT = true;
        conn.close();
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

