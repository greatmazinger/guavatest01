package net.veroy.guava.benchmark;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
// import java.nio.file.Files;
// import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class GuavaTest01 {

    public static void main(String[] args) {
        // TODO Hard-coded filepath for now TODO
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:guava_cache.db");
            stmt = conn.createStatement();
            String sql = "DROP TABLE IF EXISTS HEAP";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE HEAP" +
                  "( id INT PRIMARY KEY     NOT NULL," +
                  "  objid          INT     NOT NULL, " +
                  "  age            INT     NOT NULL, " +
                  "  alloctime      INT     NOT NULL, " +
                  "  deathtime      INT, " +
                  "  type           CHAR(4096) )";
            stmt.executeUpdate(sql);
            stmt.close();
            // TODO processInput( args[0] );
            processInput( "_201_compress-SHORT.trace" );
            conn.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Database ran successfully");
    }

    private static void processInput( String filename ) {
        try {
            int i = 0;
            String line;
            try ( // TODO hardcoded filename
                  InputStream fis = new FileInputStream("_201_compress-SHORT.trace");
                  InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                  BufferedReader br = new BufferedReader(isr);
            ) {
                while ((line = br.readLine()) != null) {
                    // Deal with the line
                    i += 1;
                    if (i % 100000 == 1) {
                        System.out.print(".");
                    } 
                }
            }
            System.out.println("");
        } catch (IOException e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }
}
