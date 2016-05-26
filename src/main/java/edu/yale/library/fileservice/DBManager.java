package edu.yale.library.fileservice;

import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.slf4j.LoggerFactory.getLogger;

public class DBManager {

    private final static Logger logger = getLogger(DBManager.class);


    public static void test(String[] a)
            throws Exception {

        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.
                getConnection("jdbc:h2:~/IdeaProjects/log_service/test", "sa", "");
        // add application code here

        //STEP 4: Execute a query
        Statement stmt = conn.createStatement();

        String sql = null;
        try {
            sql = "CREATE TABLE STUDENTS " +
                    "(studentid INTEGER not NULL, " +
                    " name VARCHAR(255))";

            stmt.executeUpdate(sql);
            logger.debug("Created table in given database...");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //STEP 4: Execute a query
        System.out.println("Inserting records into the table...");
        stmt = conn.createStatement();

        sql = "INSERT INTO STUDENTS VALUES (123456789, 'Zara')";

        for (int i = 0; i < 500000; i++) {
            stmt.executeUpdate(sql);
        }

        logger.debug("Inserted records into the table...");

        ResultSet rs;
        rs = stmt.executeQuery("select count(*) from STUDENTS");
        while (rs.next()) {
            System.out.println(rs.getInt(1));
        }

        Date d = new Date(System.currentTimeMillis());
        System.out.println("Done at:" + d.toString());


        conn.close();
    }
}

