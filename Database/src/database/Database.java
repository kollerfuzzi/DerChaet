/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author koller
 */
public class Database {
    private String DB_Driver = "";
    private String DB_Username = "";
    private String DB_Password = "";
    private String DB_Url = "";
    private Connection conn;
        
    private final String filename = "data" + File.separator + "dbproperties.properties";
    
    private static Database instance;
    
    private Database() throws ClassNotFoundException, IOException {
        loadProperties();
        Class.forName(this.DB_Driver);
    }
    
    private void loadProperties() throws IOException {
        Properties prop = new Properties();
        prop.load(getClass().getClassLoader().getResourceAsStream(filename));
        DB_Driver = prop.getProperty("driver");
        DB_Username = prop.getProperty("username");
        DB_Password = prop.getProperty("password");
        DB_Url = prop.getProperty("url");
    }
    
    public static Database getInstance() throws ClassNotFoundException, IOException {
        if(instance == null) {
            instance = new Database();
        }
        return instance;
    }
    
    public void connect(String dbname) throws SQLException {
        conn = DriverManager.getConnection(DB_Url + dbname, DB_Username, DB_Password);
    }
    
    public void disconnect() throws SQLException {
        conn.close();
    }
    
    public Connection getConn() {
        return conn;
    }
}
