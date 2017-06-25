package com.go.oscar.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.go.oscar.GoOscars;

public class ConnectionFactory {
	public static Connection getConnection() throws SQLException, ClassNotFoundException{
		try {
	        String url 		= 	GoOscars.bdUrl;
	        String user 	= 	GoOscars.bdUser;
	        String pass 	= 	GoOscars.bdPass; // pensar em em maneira de encriptar  
	        Class.forName("com.mysql.jdbc.Driver");
	        Connection con;  
	        con = DriverManager.getConnection(url, user, pass);  
	        return con;
        } catch (ClassNotFoundException e) {            
            System.out.println("O driver expecificado nao foi encontrado.");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.out.println("Nao foi possivel conectar ao banco de dados.");
            e.printStackTrace();
            return null;
        }
    }
}
