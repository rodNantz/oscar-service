package com.go.oscar.services;

import java.sql.SQLException;

import javax.ws.rs.Consumes;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.go.oscar.dao.UserDAO;
import com.go.oscar.dao.VoteDAO;
import com.go.oscar.model.MsgResponse;
import com.go.oscar.model.User;
import com.google.gson.Gson;

@Path("/vote")
public class VoteWS {

	
    @Context
    private UriInfo context;
    
	Gson gson = new Gson();
	UserDAO userDAO;
	VoteDAO voteDAO;
	

	/**
	 * POST into /vote/film: vote
	 * 
	 * @param strUsuario
	 * @return
	 */
	@POST
	@Path("/film")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response voteFilm(String strUserVote) {		
		UserVote userVote = gson.fromJson(strUserVote, UserVote.class);        
        try {
        	userDAO = new UserDAO();
        	//boolean validToken = userDAO.checkToken(userVote.getUser());
        	boolean userHasCod = userVote.getUser().getCodU() != 0;
        	if( /*validToken &&*/ userHasCod ) {
        		voteDAO = new VoteDAO();
        		if( voteDAO.vote(true, userVote.getUser(), userVote.getVote()) ) {
        			return Response.ok()
                			.entity(
                					new MsgResponse("Film vote", true, "Voto registrado!")
                			).build();
        		}
        		return Response.ok()
            			.entity(
            					new MsgResponse("Film vote", false, "Voto não registrado!")
            			).build();
        	} else {
        		return Response.ok()
                		.entity(gson.toJson(
                				new MsgResponse("Film vote", false, "Usuário inválido!"))
                		).build();
        	}
        
        } catch (SQLException sqle) {
			if( sqle.getMessage().toLowerCase().contains(voteDAO.msgAlreadyVoted.toLowerCase()) ){
				return Response.status(500)
		        		.entity(new MsgResponse("Film vote", false, "Você já votou nesta categoria!"))
		        		.build();
			}
        	sqle.printStackTrace();
	        return Response.status(500)
	        		.entity(new MsgResponse("Film vote", false, sqle.getMessage()))
	        		.build();
		} catch (Exception e) {
			e.printStackTrace();
	        return Response.status(500)
	        		.entity(new MsgResponse("Film vote", false, e.getMessage()))
	        		.build();
		}
        
	}
	

	/**
	 * POST into /vote/film: vote
	 * 
	 * @param strUsuario
	 * @return
	 */
	@POST
	@Path("/director")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response voteDirector(String strUserVote) {		
		UserVote userVote = gson.fromJson(strUserVote, UserVote.class);        
        try {
        	userDAO = new UserDAO();
        	if( userDAO.checkToken(userVote.getUser()) ) {
        		voteDAO = new VoteDAO();
        		if( voteDAO.vote(false, userVote.getUser(), userVote.getVote()) ) {
        			return Response.ok()
                			.entity(
                					new MsgResponse("Director vote", true, "Voto registrado!")
                			).build();
        		}
        		return Response.ok()
            			.entity(
            					new MsgResponse("Director vote", false, "Voto não registrado!")
            			).build();
        	} else {
        		return Response.ok()
                		.entity(gson.toJson(
                				new MsgResponse("Director vote", false, "Usuário inválido!"))
                		).build();
        	}
        } catch (SQLException sqle) {
			if( sqle.getMessage().toLowerCase().contains(voteDAO.msgAlreadyVoted.toLowerCase()) ){
				return Response.status(500)
		        		.entity(new MsgResponse("Director vote", false, "Você já votou nesta categoria!"))
		        		.build();
			}
        	sqle.printStackTrace();
	        return Response.status(500)
	        		.entity(new MsgResponse("Director vote", false, sqle.getMessage()))
	        		.build();                
		} catch (Exception e) {
			e.printStackTrace();
	        return Response.status(500)
	        		.entity(new MsgResponse("Director vote", false, e.getMessage()))
	        		.build();
		}
        
	}

	public class UserVote {
		private User user;
		private int vote;
		
		public UserVote(User user, int vote){
			this.setUser(user);
			this.setVote(vote);
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public int getVote() {
			return vote;
		}

		public void setVote(int movie) {
			this.vote = movie;
		}
		
	}	
	
}
