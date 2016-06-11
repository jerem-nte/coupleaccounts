package server;

import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class RestServer {
	
	private static Logger logger = Logger.getLogger(RestServer.class);

    public static void main(String[] args) throws Exception {

    	try {
    		PropertyConfigurator.configure(RestServer.class.getResourceAsStream("/config/log4j.properties"));
    	} catch(Exception e) {
    		logger.error("Cannot load log4j configuration file", e);
    	}
    	
    	logger.info("Starting couple account server");
    	
    	 String  baseStr  = "/html";
    	 URL     baseUrl  = RestServer.class.getResource( baseStr ); 
    	 String  basePath = baseUrl.toExternalForm();
    	 logger.info("Base path for static resources is " + basePath);
    	
    	ResourceHandler staticContext = new ResourceHandler();
        staticContext.setDirectoriesListed(false);
        staticContext.setWelcomeFiles(new String[] { "index.html" });
        staticContext.setResourceBase(basePath);
       
    	
        // Create JAX-RS application.
        final ResourceConfig application = new ResourceConfig().packages("services").register(JacksonFeature.class);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        ServletHolder jerseyServlet = new ServletHolder(new org.glassfish.jersey.servlet.ServletContainer(application));
        jerseyServlet.setInitOrder(0);
        context.addServlet(jerseyServlet, "/*");
        
        
        Server jettyServer = new Server(8083);
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { staticContext, context});
        jettyServer.setHandler(handlers);
        
        try {
            jettyServer.start();
            jettyServer.join();
        } catch (Exception e) {
            logger.error("Could not start server", e);
        } finally {
            jettyServer.destroy();
        }
    }
}
