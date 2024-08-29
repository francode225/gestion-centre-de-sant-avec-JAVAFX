package com.example.javaweb.alem.model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static java.lang.System.*;


public class LoginDB {

    private static Connection conn;

    public static Connection getConnection() throws SQLException {

        try {
            //étape 1: charger la classe de driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            String dbName = "centre_sante";


            //Online

            /*String dbServer = "mysql-172405-0.cloudclusters.net"; // database server name
            int dbPort = 10039; // change it to your database server port
            String password = "YOf6H7td";
            String userName = "admin";
            String url = "jdbc:mysql://"+dbServer+":"+dbPort+"/" + dbName;*/

            //Local

            String dbServer = "localhost"; // database server name
            String password = "";
            String userName = "root";

            String url = "jdbc:mysql://"+dbServer+":"+"/" + dbName;

            //étape 2: créer l'objet de connexion
            conn = DriverManager.getConnection(url, userName, password);
            return conn;
        } catch (Exception e) {
            out.println(e.getMessage());
        }
        return null;
    }

    public void closeConnection(Connection c) throws SQLException {
        c.close();
    }

    public static LoginDB getInstance(){
        return LoginDBHolder.loginDB;
    }

    private static class LoginDBHolder{
        private static final LoginDB loginDB = new LoginDB();
    }
}

