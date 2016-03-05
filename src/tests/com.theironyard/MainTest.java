package com.theironyard;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * Created by drewmunnerlyn on 3/5/16.
 */
public class MainTest {
    public Connection startConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./test");
        Main.createTables(conn);
        return conn;
    }

    public void endConnection(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DROP TABLE users");
        stmt.execute("DROP TABLE beers");
        conn.close();
    }

    @Test
    public void testUser() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Drew", "123");
        User user = Main.selectUser(conn, "Drew");
        endConnection(conn);
        assertTrue(user != null);
    }

    @Test
    public void testEntry() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Drew", "123");
        User user = Main.selectUser(conn, "Drew");
        Main.insertEntry(conn, user.id, "Sierra Nevada", "Pale Ale", 6, true, "the beer was great!", "https://goo.gl/l2bdpa");
        Beer beer = Main.selectEntry(conn, 1);
        endConnection(conn);
        assertTrue(beer != null);
    }

    @Test
    public void testEntries() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Drew", "");
        Main.insertUser(conn, "Bob", "");
        User drew = Main.selectUser(conn, "Drew");
        User bob = Main.selectUser(conn, "Bob");
        Main.insertEntry(conn, drew.id, "Sierra Nevada", "Pale Ale", 6, true, "the beer was great!", "https://goo.gl/l2bdpa");
        Main.insertEntry(conn, bob.id, "Sierra Nevada", "Porter", 4, false, "the beer was sooo bad!", "https://goo.gl/l2bdpa");
        ArrayList<Beer> beers = Main.selectEntries(conn);
        endConnection(conn);
        assertTrue(beers.size() == 2);
    }

//    @Test
//    public void testUpdateEntry() throws SQLException {
//        Connection conn = startConnection();
//        User drew = Main.selectUser(conn, "Drew");
//        Main.insertEntry(conn, drew.id, "Sierra Nevada", "Pale Ale", 6, true, "the beer was great!", "https://goo.gl/l2bdpa");
//        Beer beer = Main.selectEntry(conn, 1);
//        beer.comment = "I changed my mind, the beer was badddd";
//        Main.updateEntry(conn, "Sierra Nevada", "Pale Ale", 6, true, "I changed my mind, the beer was badddd", "https://goo.gl/l2bdpa", 1);
//        Beer beerUpdated = Main.selectEntry(conn, 1);
//        endConnection(conn);
//        assertTrue(beerUpdated.comment.equals("I changed my mind, the beer was badddd"));
//    }

    @Test
    public void testDeleteEntry() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Drew", "123");
        User drew = Main.selectUser(conn, "Drew");
        Main.insertEntry(conn, drew.id, "Sierra Nevada", "Pale Ale", 6, true, "the beer was great!", "https://goo.gl/l2bdpa");
        Beer beer = Main.selectEntry(conn, 1);
        Main.deleteEntry(conn, drew.id);
        Beer deletedEntry = Main.selectEntry(conn, 1);
        endConnection(conn);
        assertTrue(deletedEntry == null);

    }


}
