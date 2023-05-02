package com.abelmazov;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnect {           //connect to DataBase
    public static final String LOGIN = "root";
    public static final String PASSWORD = "root";
    public static final String LINK = "jdbc:mysql://localhost:3306/salonkrasoti";
    public static Statement statement;
    public static Connection connection;
    public static DbConnect instance;

    private DbConnect(){};
    public static DbConnect getInstance(){
      if(instance==null){
          instance = new DbConnect();
      }
        return instance;
    };

    static {
        try {
            connection = DriverManager.getConnection(LINK,LOGIN,PASSWORD);
            statement = connection.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
