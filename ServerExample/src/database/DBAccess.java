/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import beans.Message;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

/**
 *
 * @author koller
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editIor.
 */

public class DBAccess {

    private final String selectUserExistsStr = "SELECT COUNT(*) \"count\""
            + "FROM chatuser "
            + "WHERE username = ? AND pwd = ? AND enabled = TRUE";
    private PreparedStatement selectUserExists = null;

    private final String insertUserStr
            = "INSERT INTO ChatUser (username, pwd, email, enabled) "
            + "VALUES(?, ?, ?, TRUE);";
    private PreparedStatement insertUser = null;

    private final String disableUserStr = "UPDATE ChatUser\n"
            + "SET enabled = FALSE\n"
            + "WHERE username = ?;";
    private PreparedStatement disableUser = null;

    private final String insertMessageStr = "INSERT INTO Message "
            + "(sendtime, source_user, message) VALUES(?, ?, ?);";
    private PreparedStatement insertMessage = null;

    private final String selectMessagesStr = "SELECT * "
            + "FROM message "
            + "WHERE id > (SELECT MAX(id) FROM message) - ? "
            + "ORDER BY id;";
    private PreparedStatement selectMessages = null;

    private Database db;

    /**
     * Creates a new DBAccess object
     * @param dbname Name of the Database
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IOException 
     */
    public DBAccess(String dbname) throws SQLException, ClassNotFoundException, IOException {
        db = Database.getInstance();
        db.connect(dbname);
    }

    /**
     * Checks if the user credentials are correct, and returns true if so.
     *
     * @param username Username
     * @param password User Password
     * @return
     * @throws SQLException
     */
    public boolean checkUserCredentials(String username, String password) throws SQLException {
        if (selectUserExists == null) {
            selectUserExists = db.getConn().prepareStatement(selectUserExistsStr);
        }
        selectUserExists.setString(1, username);
        selectUserExists.setString(2, password);
        ResultSet rs = selectUserExists.executeQuery();
        rs.next();
        return rs.getInt("count") > 0;
    }

    /**
     * Registers a user, enables him, and stores him in the database
     *
     * @param username Username
     * @param password Password
     * @param email Email-Address f.e.: pussid14@htlkaindorf.at
     * @throws SQLException
     */
    public void registerUser(String username, String password, String email) throws SQLException {
        if (insertUser == null) {
            insertUser = db.getConn().prepareStatement(insertUserStr);
        }
        insertUser.setString(1, username);
        insertUser.setString(2, password);
        insertUser.setString(3, email);
        insertUser.executeUpdate();
    }

    /**
     * Sets the "enabled" value of the user to FALSE, so that he can't login
     * anymore
     *
     * @param username The username of the User
     * @throws SQLException
     */
    public void disableUser(String username) throws SQLException {
        if (disableUser == null) {
            disableUser = db.getConn().prepareStatement(disableUserStr);
        }
        disableUser.setString(1, username);
        disableUser.executeUpdate();
    }

    /**
     * Stores a message in the database
     *
     * @param msg The message to store
     */
    public void storeMessage(Message msg) throws SQLException {
        if (insertMessage == null) {
            insertMessage = db.getConn().prepareStatement(insertMessageStr);
        }
        insertMessage.setString(1, msg.getUsername());
        insertMessage.setString(2, msg.getMessage());
        insertMessage.setTimestamp(3, new Timestamp(
                msg.getTimestamp().toEpochSecond(ZoneOffset.UTC)));
        insertMessage.executeUpdate();
    }

    /**
     * Gets the last N messages from the database
     *
     * @param count n
     */
    public ArrayList<Message> getLastMessages(int count) throws SQLException {
        ArrayList<Message> messages = new ArrayList<>();
        if(selectMessages == null)  {
            selectMessages = db.getConn().prepareStatement(selectMessagesStr);
        }
        selectMessages.setInt(1, count);
        ResultSet rs = selectMessages.executeQuery();
        while (rs.next()) {
            String username = rs.getString("source_user");
            String message = rs.getString("message");
            LocalDateTime timestamp = 
                    rs.getTimestamp("sendtime").toLocalDateTime();
            messages.add(new Message(username, message, timestamp));
        }
        return messages;
    }

    /**
     * Disconnects from the database
     * @throws SQLException 
     */
    public void disconnect() throws SQLException {
        db.disconnect();
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        DBAccess acc = new DBAccess("chaet");
        System.out.println(acc.checkUserCredentials("gg", "grassmugg"));
        System.out.println(acc.checkUserCredentials("Elias", "kollerfuzzi"));
        System.out.println(acc.checkUserCredentials("karl", "mayer"));
        
        //acc.registerUser("karl", "mayer", "karl.mayer@gmail.com");
        //acc.disableUser("karl");
        acc.getLastMessages(50).forEach(System.out::println);
        acc.disconnect();
    }
}
