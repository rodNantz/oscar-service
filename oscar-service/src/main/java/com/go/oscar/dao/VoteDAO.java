package com.go.oscar.dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.go.oscar.model.User;

public class VoteDAO {

	MessageDigest md;
	private final String stmtVoteForFilm = "INSERT INTO filmvote (codu, codf) VALUES(?, ?)";
	private final String stmtVoteForDirector = "INSERT INTO dirvote (codu, codd) VALUES(?, ?)";
	   
    public VoteDAO(){
    	 try {
			this.md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
    
    public boolean vote(boolean isForFilm, User user, int vote) throws SQLException, Exception {
        Connection con = null;
        PreparedStatement stmt = null;
        String voteStmt = isForFilm ? stmtVoteForFilm : stmtVoteForDirector; 
        try{
            con = ConnectionFactory.getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement(voteStmt);
            stmt.setLong(1, user.getCodU());
            stmt.setInt(2, vote);
            stmt.executeUpdate();
            
            con.commit();
            
            return true;
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
        
	
}
