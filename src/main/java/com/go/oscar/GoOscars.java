package com.go.oscar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.util.Collections;
import java.util.EnumSet;
import javax.servlet.DispatcherType;

import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Password;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import com.go.oscar.util.PropertySource;
import com.google.gson.Gson;


public class GoOscars {

	private static Server server;
	private static String host = "localhost";
	private static int port = 8080;
	public static String bdUrl;
    public static String bdUser;
    public static String bdPass;
    private static String servicesPkg = "";
    private static String servicesPath = "";
    
	public static void main(String[] args) throws Exception {
		launch();
	}
	
	public static void launch() throws IOException, Exception{
		
		String buildPath = new File("").getAbsolutePath();
		System.out.println("* Build path: " + buildPath);
	
		servicesPkg = PropertySource.props.getProperty("oscars.service.pkg");
		servicesPath = PropertySource.props.getProperty("oscars.service.path");
		bdUrl = PropertySource.props.getProperty("oscars.bd.url");
        bdUser = PropertySource.props.getProperty("oscars.bd.user");
        bdPass = PropertySource.props.getProperty("oscars.bd.pass");
        host = PropertySource.props.getProperty("oscars.launch.host");
        
        if(!host.equalsIgnoreCase("localhost")) {
        	host = Inet4Address.getLocalHost().getHostAddress();
        }
		try {
			// if the port is a number
			port = Integer.valueOf(PropertySource.props.getProperty("oscars.launch.port"));
		} catch(NumberFormatException nfe){
			//if it is a string (for customization)
			String strPort = PropertySource.props.getProperty("oscars.launch.port");
			if(strPort.equalsIgnoreCase("heroku")){
				 port = Integer.valueOf(System.getenv("PORT"));
			}
		}
	
		server = new Server(port);
		HandlerList a = new HandlerList();
		
        //webservices
		ResourceConfig config = new ResourceConfig();
		config.register(MultiPartFeature.class);
		config.packages(
				servicesPkg
				);
		config.register(JacksonFeature.class);
		ServletHolder servlet = new ServletHolder(new ServletContainer(config));
		ServletContextHandler svrContext = new ServletContextHandler(server, servicesPath);
		svrContext.addServlet(servlet, "/*"); //server/endpoints
		svrContext.setInitParameter("jersey.config.server.provider.packages", "com.jersey.jaxb,com.fasterxml.jackson.jaxrs.json");
		svrContext.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
		
		//CORS
		FilterHolder holder = new FilterHolder(new CrossOriginFilter());
		holder.setInitParameter("allowedMethods", "GET,POST,PUT,DELETE,HEAD,OPTIONS");
		//holder.setInitParameter("allowedOrigins", "http://localhost:4200");
		holder.setInitParameter("allowedHeaders", "Content-Type, Accept, X-Requested-With");
		svrContext.addFilter(holder, "/*", EnumSet.of(DispatcherType.REQUEST));
		
		a.addHandler(svrContext);
		server.setHandler(a);
		
		try {
			server.start();
			System.out.println("* Up at "+ host +":"+ port);
			server.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

}
