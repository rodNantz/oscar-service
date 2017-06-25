package com.go.oscar.dao;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.go.oscar.model.User;

public class UserDAO {

	MessageDigest md;
	private final String stmtSearchLogin = "SELECT * FROM user WHERE user=? AND pass=?";
	private final String stmtDoLogin = "UPDATE user SET logged=true WHERE codu=?";
	private final String stmtDoLogout = "UPDATE user SET logged=false WHERE codu=?";
	   
    public UserDAO(){
    	 try {
			this.md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
    
    public User login(User user) throws SQLException, Exception {
        Connection con = null;
        PreparedStatement stmt = null;
        try{
            con = ConnectionFactory.getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement(stmtSearchLogin);
            stmt.setString(1, user.getUser());
            stmt.setString(2, user.getPass());
            stmt.executeQuery();
            
            ResultSet rs = stmt.getResultSet();  
            if(rs.next()){
            	user.setCodU(rs.getInt("codU"));
            	user.setToken(makeToken(user));
            	user.setPass("");
            	if(!dologinUpdate(user)){
            		user = null;
            	}
            } else {
            	user = null;
            }
            rs.close();
            con.commit();
            
            return user;
        } catch (SQLException ex) {
            try{con.rollback();}catch(SQLException ex1){System.out.println("Erro ao tentar rollback. Ex="+ex1.getMessage());};
            throw new SQLException("Erro ao inserir um usuário no banco de dados. Origem = "+ex.getMessage());
        } finally{
            try{stmt.close();}catch(Exception ex){
            	throw new Exception("Erro ao fechar stmt. Ex="+ex.getMessage());
            	};
            try{con.close();;}catch(Exception ex){
            	throw new Exception("Erro ao fechar conexão. Ex="+ex.getMessage());
            	};
        }
    }
    
    private boolean dologinUpdate(User user) throws SQLException, Exception {
        Connection con = null;
        PreparedStatement stmt = null;
        try{
            con = ConnectionFactory.getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement(stmtDoLogin);
            stmt.setLong(1, user.getCodU());
            stmt.executeUpdate();
            
            con.commit();
            
            return true;
        } catch (SQLException ex) {
            try{con.rollback();}catch(SQLException ex1){System.out.println("Erro ao tentar rollback. Ex="+ex1.getMessage());};
            ex.printStackTrace();
            throw new SQLException("Erro ao logar no banco de dados. Origem = "+ex.getMessage());
        } finally{
            try{stmt.close();}catch(Exception ex){
            	ex.printStackTrace();
            	throw new Exception("Erro ao fechar stmt. Ex="+ex.getMessage());
            	};
            try{con.close();;}catch(Exception ex){
            	ex.printStackTrace();
            	throw new Exception("Erro ao fechar conexão. Ex="+ex.getMessage());
            	};
        }
    }
    
    public boolean logout(User user) throws SQLException, Exception{
        Connection con = null;
        PreparedStatement stmt = null;
        try{
            con = ConnectionFactory.getConnection();
            stmt = con.prepareStatement(stmtDoLogout);
            stmt.setLong(1, user.getCodU());
            
            stmt.executeUpdate();
            
            return true;
        }catch(SQLException ex){
        	ex.printStackTrace();
            throw new SQLException("Logout error: " +ex.getMessage());
        }finally{
            try{stmt.close();}catch(Exception ex){
            	throw new Exception("Erro ao fechar stmt. Ex = " +ex.getMessage());
            	};
            try{con.close();;}catch(Exception ex){
            	throw new Exception("Erro ao fechar conexão. Ex= "+ex.getMessage());
            	};
        }
    }
    
    
    public String makeToken(User user) throws NoSuchAlgorithmException{
    	MessageDigest userMd = md;
    	userMd.update(
				 (user.getCodU() + "|" + user.getUser())
				 .getBytes());
       return bytesToHex(userMd.digest());
    }
    
    public boolean checkToken(User user) throws NoSuchAlgorithmException{
    	MessageDigest userMd = md;
    	userMd.update(
				 (user.getCodU() + user.getUser())
				 .getBytes());
    	String valid = printBytes(makeToken(user));
    	String userT = printBytes(user.getToken());
    	System.out.println(valid + "\n" + userT);
    	return valid.equalsIgnoreCase(userT);
    }
    
	private String bytesToHex(byte[] bytes) {
		StringBuffer result = new StringBuffer();
	    for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
	    return result.toString();
	}
    
	private String printBytes(String str) {
		Charset ENCODING = Charset.forName("UTF-8");
	    byte[] bytes = str.getBytes(ENCODING);
	    String output = "byte[";
	    for (int i = 0; i < bytes.length; i++) {
	        output += Byte.toString(bytes[i]);
	        if (i < bytes.length - 1) {
	            output += ", ";
	        }
	    }
	    output += "]";
	    return output;
	}
	
}
