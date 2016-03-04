package com.theironyard;

import jodd.json.JsonSerializer;
import spark.ModelAndView;
import spark.Session;
import spark.Spark;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    static ArrayList<Beer> beers = new ArrayList<>();

    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, name VARCHAR, password VARCHAR)");

        stmt.execute("CREATE TABLE IF NOT EXISTS beers (id IDENTITY, user_id INT, beer_name VARCHAR, beer_type VARCHAR, alcohol_content INT, is_good BOOLEAN, comment VARCHAR)");
    }

    public static void insertUser(Connection conn, String name, String password) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?, ?)");
        stmt.setString(1, name);
        stmt.setString(2, password);
        stmt.execute();
    }

    public static User selectUser(Connection conn, String name) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE name = ?");
        stmt.setString(1, name);
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            int id = results.getInt("id");
            String password = results.getString("password");
            return new User(id, name, password);
        }
        return null;
    }

    public static void insertEntry(Connection conn, int userId, String beerName, String beerType, int alcoholContent, boolean isGood, String comment) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO beers VALUES (NULL, ?, ?, ?, ?, ?, ?)");
        stmt.setInt (1, userId);
        stmt.setString(2, beerName);
        stmt.setString(3, beerType);
        stmt.setInt(4, alcoholContent);
        stmt.setBoolean(5, isGood);
        stmt.setString(6, comment);
        stmt.execute();
    }

    public static Beer selectEntry(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM beers INNER JOIN users ON beers.user_id = users.id WHERE beers.id = ?");
        stmt.setInt(1, id);
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            String name = results.getString("users.name");
            String beerName = results.getString("beers.beer_name");
            String beerType = results.getString("beers.beer_type");
            int alcoholContent = Integer.valueOf(results.getString("beers.alcohol_content"));
            boolean isGood = results.getBoolean("beers.is_good");
            String comment = results.getString("beers.comment");
            Beer beer = new Beer(id, name, beerName, beerType, alcoholContent, isGood, comment);
            return beer;
        }
        return null;
    }

    public static ArrayList<Beer> selectEntries(Connection conn) throws SQLException {
        ArrayList<Beer> beers = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM beers INNER JOIN users ON beers.user_id = users.id");
        ResultSet results = stmt.executeQuery();
        while (results.next()) {
            int id = results.getInt("beers.id");
            String author = results.getString("users.name");
            String beerName = results.getString("beers.beer_name");
            String beerType = results.getString("beers.beer_type");
            int alcoholContent = results.getInt("beers.alcohol_content");
            boolean isGood = results.getBoolean("beers.is_good");
            String comment = results.getString("beers.comment");


            Beer beer = new Beer(id, author, beerName, beerType, alcoholContent, isGood, comment);
            beers.add(beer);
        }
        return beers;
    }

    public static void deleteEntry(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM beers WHERE id =?");
        stmt.setInt(1, id);
        stmt.execute();
    }

    public static void updateEntry(Connection conn, String beerName, String beerType, int alcoholContent, boolean isGood, String comment, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE messages SET beer_name = ?, beer_type = ?," +
                "alcohol_content= ?, is_good= ?, comment = ? WHERE id = ?");
        stmt.setString(1, beerName);
        stmt.setString(2, beerType);
        stmt.setInt(3, alcoholContent);
        stmt.setBoolean(4, isGood);
        stmt.setString(5, comment);
        stmt.setInt(6, id);
        stmt.execute();
    }

    public static void main(String[] args) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);

        if (selectEntries(conn).size() == 0) {
            insertUser(conn, "Drew", "123");
            insertEntry(conn, 1, "Amstel", "Pilsner", 6, true, "its a great beer");
        }

        Spark.staticFileLocation("public");
        Spark.init();
        Spark.get(
                "/",
                ((request, response) -> {

                    Session session = request.session();
                    String userName = session.attribute("userName");
                    ArrayList<Beer> beers = selectEntries(conn);

                    JsonSerializer serializer = new JsonSerializer();
                    return serializer.serialize(beers);
                })
            );
        Spark.post(
                "/login",
                ((request, response) -> {
                    String name = request.queryParams("loginName");
                    String password = request.queryParams("userPassword");

                    User user = selectUser(conn, name);
                    if (user == null) {
                        insertUser(conn, name, password);
                        Session session = request.session();
                        session.attribute("userName", name);
                        response.redirect("/");
                    }
                    else if (user.password.equals(password)) {
                        Session session = request.session();
                        session.attribute("userName", name);
                        response.redirect("/");
                    }
                    else {
                        response.redirect("/");
                    }
                    return "";
                })
        );
        Spark.post(
                "/logout",
                ((request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                })
        );
        Spark.post(
                "/create-beer",
                ((request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("userName");


                    User user = selectUser(conn, userName);
                    String beerName = request.queryParams("beers.beer_name");
                    String beerType = request.queryParams("beers.beer_type");
                    int alcoholContent = Integer.valueOf(request.queryParams("beers.alcohol_content"));
                    boolean isGood = Boolean.valueOf(request.queryParams("beers.is_good"));
                    String comment = request.queryParams("beers.comment");

                    insertEntry(conn, user.id, beerName, beerType, alcoholContent, isGood, comment);

                    response.redirect("/");

                    return "";
                })
        );
        Spark.post(
                "/delete-beer",
                ((request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("userName");

                    if (userName == null) {
                        throw new Exception("Not logged in");
                    }

                    String beerId = request.queryParams("deleteId");
                    if (beerId == null) {
                        throw new Exception("didn't get nec. params");
                    }
                    int beerIdNum = Integer.valueOf(beerId);
                    for (int i = 0; i < beers.size(); i ++){
                        beers.get(i).id = i;
                    }

                    deleteEntry(conn, beerIdNum);

                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/edit-beer",
                ((request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("userName");
                    if (userName == null) {
                        throw new Exception("Not logged in");
                    }
                    int id = Integer.valueOf(request.queryParams("id"));
                    Beer editBeer = selectEntry(conn, id);

                    String beerName = request.queryParams("editBeerName");
                    String beerType = request.queryParams("editBeerType");
                    int alcoholContent = Integer.valueOf(request.queryParams("editAlcoholContent"));
                    boolean isGood = Boolean.valueOf(request.queryParams("editIsGood"));
                    String comment = request.queryParams("editComment");


                    updateEntry(conn, beerName, beerType, alcoholContent, isGood, comment, id);

                    response.redirect("/");
                    return "";
                })
        );

        }
    }

