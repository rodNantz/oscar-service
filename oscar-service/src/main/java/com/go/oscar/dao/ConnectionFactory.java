package com.go.oscar.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.go.oscar.GoOscars;

public class ConnectionFactory {
	public static Connection getConnection() throws SQLException, ClassNotFoundException{
        String url 		= 	GoOscars.bdUrl;
        String user 	= 	GoOscars.bdUser;
        String pass 	= 	GoOscars.bdPass; // pensar em em maneira de encriptar  
        Class.forName("com.mysql.jdbc.Driver");
        Connection con;  
        con = DriverManager.getConnection(url, user, pass);  
        return con;
    }
}
