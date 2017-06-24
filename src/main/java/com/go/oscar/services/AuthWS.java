package com.go.oscar.services;

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.go.oscar.dao.UserDAO;
import com.go.oscar.model.MsgResponse;
import com.go.oscar.model.User;
import com.google.gson.Gson;

@Path("/auth")
public class AuthWS {

	
    @Context
    private UriInfo context;
    
	Gson gson = new Gson();
	UserDAO userDAO;
	

	/**
	 * POST into /auth: login
	 * 
	 * @param strUsuario
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(String strUser) {		
		EmailPass creds = gson.fromJson(strUser, EmailPass.class);        
        try {
        	userDAO = new UserDAO();
            User search = new User (0, creds.getUser(), creds.getPass(), "");
            User found = userDAO.login(search);
        	if (found != null){
        		return Response.ok()
        			.entity(
        					new MsgResponse("login", true, "Login realizado!", gson.toJson(found))
        			).build();
        	}
        	return Response.ok()
            		.entity(gson.toJson(
            				new MsgResponse("login", false, "Dados inválidos!"))
            		).build();
            
		} catch (Exception e) {
			e.printStackTrace();
	        return Response.status(500)
	        		.entity(new MsgResponse("login", false, e.getMessage()))
	        		.build();
		}
        
	}

	
	/*
	 * DELETE into /auth : logout
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(String strUser) throws SQLException, ClassNotFoundException{
		User user = gson.fromJson(strUser, User.class);        
        try {
        	userDAO = new UserDAO();
            boolean out = userDAO.logout(user);
        	if (out){
        		return Response.ok()
        			.entity(
        					new MsgResponse("logout", true, "Logout realizado!")
        			).build();
        	}
        	return Response.ok()
            		.entity(gson.toJson(
            				new MsgResponse("logout", false, "Logout não pôde ser realizado!"))
            		).build();
            
		} catch (Exception e) {
			e.printStackTrace();
	        return Response.status(500)
	        		.entity(new MsgResponse("logout", false, e.getMessage()))
	        		.build();
		}
	}
	
	
	public class EmailPass {
		private String user;
		private String pass;
		
		public EmailPass(String user, String pass){
			this.setUser(user);
			this.setPass(pass);
		}

		public String getUser() {
			return user;
		}

		public void setUser(String user) {
			this.user = user;
		}

		public String getPass() {
			return pass;
		}

		public void setPass(String residuo) {
			this.pass = residuo;
		}
		
	}
	
}
