package com.go.oscar.dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.go.oscar.model.User;
import com.go.oscar.model.Vote;

public class VoteDAO {

	MessageDigest md;
	private final String stmtCheckIfUserVoted = "SELECT * FROM vote WHERE codu = ?";
	private final String stmtVote = "INSERT INTO vote (codu, vote_film, vote_dir) VALUES(?, ?, ?)";
	public final String msgAlreadyVoted = "Duplicate Entry";
	   
    public VoteDAO(){
    	 try {
			this.md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
    
    public boolean vote(Vote vote) throws SQLException, Exception {
        Connection con = null;
        PreparedStatement stmt = null; 
        try{
            con = ConnectionFactory.getConnection();
            con.setAutoCommit(false);
            stmt = con.prepareStatement(stmtVote);
            stmt.setLong(1, vote.getUser().getCodU());
            stmt.setInt(2, vote.getVoteFilm());
            stmt.setInt(3, vote.getVoteDir());
            stmt.executeUpdate();
            
            con.commit();
            
            return true;
        } catch (SQLException ex) {
            try{con.rollback();}catch(SQLException ex1){System.out.println("Erro ao tentar rollback. Ex="+ex1.getMessage());};
            throw new SQLException("Erro ao inserir um voto no banco de dados. Origem = "+ex.getMessage());
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
    
    
    public boolean checkIfVoted(User user) throws SQLException, Exception {
        Connection con = null;
        PreparedStatement stmt = null;
        try{
            con = ConnectionFactory.getConnection();
            stmt = con.prepareStatement(stmtCheckIfUserVoted);
            stmt.setLong(1, user.getCodU());
            stmt.executeQuery();
            
            ResultSet rs = stmt.getResultSet();  
            if(rs.next()){
            	rs.close();
            	return true;
            } else {
            	rs.close();
            	return false;
            }
        } catch (SQLException ex) {
            try{con.rollback();}catch(SQLException ex1){System.out.println("Erro ao tentar rollback. Ex="+ex1.getMessage());};
            ex.printStackTrace();
            throw new SQLException("Erro ao inserir um usuário no banco de dados. Origem = "+ex.getMessage());
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
        
	
}
