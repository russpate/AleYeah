package com.theironyard;

import jodd.json.JsonSerializer;
import spark.Session;
import spark.Spark;

import java.sql.*;
import java.util.ArrayList;

public class Main {
    static ArrayList<Beer> beers = new ArrayList<>();
//test
    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, name VARCHAR, password VARCHAR)");

        stmt.execute("CREATE TABLE IF NOT EXISTS beers (id IDENTITY, user_id INT, beer_name VARCHAR, beer_type VARCHAR, alcohol_content VARCHAR, is_good BOOLEAN, comment VARCHAR)"); //took out , image VARCHAR
    }

    public static void insertUser(Connection conn, String name, String password) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?, ?)");
        stmt.setString(1, name);
        stmt.setString(2, password);
        stmt.executeUpdate();
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

    public static void insertEntry(Connection conn, int userId, String beerName, String beerType, String alcoholContent, boolean isGood, String comment) throws SQLException { //took out , String image
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO beers VALUES (NULL, ?, ?, ?, ?, ?, ?)"); //took out one ? question mark
        stmt.setInt (1, userId);
        stmt.setString(2, beerName);
        stmt.setString(3, beerType);
        stmt.setString(4, alcoholContent);
        stmt.setBoolean(5, isGood);
        stmt.setString(6, comment);
//        stmt.setString(7, image);
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
            String alcoholContent = results.getString("beers.alcohol_content");
            boolean isGood = results.getBoolean("beers.is_good");
            String comment = results.getString("beers.comment");
//            String image = results.getString("beers.image");
            Beer beer = new Beer(id, name, beerName, beerType, alcoholContent, isGood, comment); //, image
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
            String alcoholContent =results.getString("beers.alcohol_content");
            boolean isGood = results.getBoolean("beers.is_good");
            String comment = results.getString("beers.comment");
//            String image = results.getString("beers.image");

            Beer beer = new Beer(id, author, beerName, beerType, alcoholContent, isGood, comment); //, image
            beers.add(beer);
        }
        return beers;
    }

    public static ArrayList<User> selectUsers(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users");
        ArrayList<User> users = new ArrayList<>();
        ResultSet results = stmt.executeQuery();
        while (results.next()) {
            int id = Integer.valueOf(results.getString("id"));
            String name = results.getString("name");
            String password = results.getString("password");
            User user = new User(id, name, password);
            users.add(user);
        }
        return users;
    }

    public static void deleteEntry(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM beers WHERE id =?");
        stmt.setInt(1, id);
        stmt.execute();
    }

    public static void updateEntry(Connection conn, String beerName, String beerType, String alcoholContent, boolean isGood, String comment, int id) throws SQLException { //, String image
        PreparedStatement stmt = conn.prepareStatement("UPDATE messages SET beer_name = ?, beer_type = ?," +
                "alcohol_content= ?, is_good= ?, comment = ? WHERE id = ?"); //, image = ?
        stmt.setString(1, beerName);
        stmt.setString(2, beerType);
        stmt.setString(3, alcoholContent);
        stmt.setBoolean(4, isGood);
        stmt.setString(5, comment);
//        stmt.setString(6, image);
        stmt.setInt(7, id);
        stmt.execute();
    }

    public static void main(String[] args) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);

        insertUser(conn, "sal", "amander");
        if (selectEntries(conn).size() == 0) {
            insertUser(conn, "Drew", "123");
            insertEntry(conn, 1, "Amstel", "Pilsner", "6", true, "its a great beer"); //, "https://goo.gl/l2bdpa"
        }

        Spark.externalStaticFileLocation("public");
        Spark.init();

        Spark.get(
                "/users",
                ((request1, response1) -> {
                    JsonSerializer s = new JsonSerializer();
                    return s.serialize(selectUsers(conn));

                })

        );

        Spark.get(
                "/get-beers",
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
//                        return "Username not valid, you need to create a username";
                        insertUser(conn, name, password);
                        Session session = request.session();
                        session.attribute("userName", name);

                    }
                    else if (user.password.equals(password)) {
                        Session session = request.session();
                        session.attribute("userName", name);

                    }

                    return "";
                })
        );
//        Spark.post(
//                "/create-user",
//                ((request, response) -> {
//                    String name = request.queryParams("loginName");
//                    String password = request.queryParams("userPassword");
//
//                    User user = selectUser(conn, name);
//                    if (user == null) {
//                        insertUser(conn, name, password);
//                        Session session = request.session();
//                        session.attribute("userName", name);
//
//                    }
//                    else {
//                        return "User already exists";
//
//                    }
//
//                    return "";
//                })
//        );

        Spark.post(
                "/logout",
                ((request, response) -> {
                    Session session = request.session();
                    session.invalidate();

                    return "";
                })
        );
        Spark.post(
                "/create-beer",
                ((request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("userName");


                    User user = selectUser(conn, userName);
                    String beerName = request.queryParams("beerName");
                    String beerType = request.queryParams("beerType");
                    String alcoholContent = request.queryParams("alcoholContent");
                    boolean isGood = Boolean.valueOf(request.queryParams("isGood"));
                    String comment = request.queryParams("comment");
//                    String image = request.queryParams("beers.image");

                    insertEntry(conn, user.id, beerName, beerType, alcoholContent, isGood, comment); //, image



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
                    //Beer editBeer = selectEntry(conn, id);

                    String beerName = request.queryParams("editBeerName");
                    String beerType = request.queryParams("editBeerType");
                    String alcoholContent = request.queryParams("editAlcoholContent");
                    boolean isGood = Boolean.valueOf(request.queryParams("editIsGood"));
                    String comment = request.queryParams("editComment");
//                    String image = request.queryParams("editImage");

                    updateEntry(conn, beerName, beerType, alcoholContent, isGood, comment, id); //, image

                    return "";
                })
        );
        Spark.get(
                "/search-beer",
                ((request, response) -> {


                    ArrayList<Beer> searchBeers = new ArrayList<>();
                    String searchName = request.queryParams("beerSearch");
                    String searchType = request.queryParams("typeSearch");

                    if ((searchName != null && !searchName.isEmpty()) || (searchType!=null && !searchType.isEmpty())) {
                        ArrayList<Beer> allBeer = selectEntries(conn);
                        for (Beer beer: allBeer) {
                            if (beer.beerName.toLowerCase().contains(searchName.toLowerCase()) && beer.beerType.toLowerCase().contains(searchType.toLowerCase())){
                                searchBeers.add(beer);
                            }
                        }
                    }

                    else if (searchName != null && !searchName.isEmpty()) {
                        ArrayList<Beer> allBeer = selectEntries(conn);
                        for (Beer beer: allBeer) {
                            if (beer.beerName.toLowerCase().contains(searchName.toLowerCase())){
                                searchBeers.add(beer);
                            }
                        }
                    }

                    else if (searchType != null && !searchType.isEmpty()) {
                        ArrayList<Beer> allBeer = selectEntries(conn);
                        for (Beer beer: allBeer) {
                            if (beer.beerType.toLowerCase().contains(searchType.toLowerCase())){
                                searchBeers.add(beer);
                            }
                        }
                    }
                    JsonSerializer serializer = new JsonSerializer();
                    return serializer.serialize(searchBeers);
            })
        );


        }
    }
