package net.veroy.guava.benchmark;

import net.veroy.guava.benchmark.ObjectRecord;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class GuavaTest01 {
    private static Cache<Integer, ObjectRecord> cache;
    private static Connection conn;

    public static void main(String[] args) {
        // TODO Hard-coded filepath for now TODO
        cache = CacheBuilder.newBuilder()
            .maximumSize(140000000)
            .build();
        conn = null;
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

    private static ObjectRecord getFromDB( int objId ) throws SQLException {
        ObjectRecord objrec = new ObjectRecord();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery( String.format("SELECT * FROM HEAP WHERE objid=%d;", objId) );
        if (rs.next()) {
            objrec.set_objId( rs.getInt("objid") );
            objrec.set_age( rs.getInt("age") );
            objrec.set_allocTime( rs.getInt("alloctime") );
            objrec.set_deathTime( rs.getInt("deathtime") );
            objrec.set_myType( rs.getString("type") );
        } else {
            objrec.set_objId( objId );
        }
        return objrec;
    }

    private static void processInput( String filename ) {
        try {
            int i = 0;
            String line;
            try ( // TODO hardcoded filename
                  // InputStream fis = new FileInputStream( filename );
                  InputStream fis = new FileInputStream("fop-SHORT-20M.trace");
                  InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                  BufferedReader bufreader = new BufferedReader(isr);
            ) {
                int timeByMethod = 0;
                while ((line = bufreader.readLine()) != null) {
                    // Deal with the line
                    String[] fields = line.split(" ");
                    if (isAllocation(fields[0])) {
                        ObjectRecord rec = parseAllocation( fields, timeByMethod );
                        int objId = rec.get_objId();
                        try {
                            ObjectRecord dbrec = cache.get(objId, new Callable<ObjectRecord>() {
                                @Override
                                public ObjectRecord call() throws SQLException {
                                    return getFromDB(objId);
                                }
                            });
                        } catch (ExecutionException e) {
                            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                            System.err.println( "Continuing..." );
                        }
                    }

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

    private static boolean isAllocation( String op ) {
        return (op.equals("A") || op.equals("N") || op.equals("P") || op.equals("I"));
    }

    private static ObjectRecord parseAllocation( String[] fields, int timeByMethod ) {
        // System.out.println("[" + fields[0] + "]");
        int objId = Integer.parseInt( fields[1], 16 );
        String type = fields[3];
        // UNUSED right now:
        // int size = Integer.parseInt( fields[2], 16 );
        // int site = Integer.parseInt( fields[4], 16 );
        // int length = Integer.parseInt( fields[5], 16 );
        // int threadId = Integer.parseInt( fields[6], 16 );
        return new ObjectRecord( 0, // Autogenerated by database
                                 objId,
                                 0, // Unknown at this point
                                 timeByMethod,
                                 0, // Unknown at this point
                                 type );
    }
}
