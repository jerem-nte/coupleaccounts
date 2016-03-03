package server;

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

    	PropertyConfigurator.configure("src/main/resources/log4j.properties");
    	
    	logger.info("Test");
    	
    	ResourceHandler staticContext = new ResourceHandler();
        staticContext.setDirectoriesListed(true);
        staticContext.setWelcomeFiles(new String[] { "index.html" });
        staticContext.setResourceBase("./html");
       
    	
        // Create JAX-RS application.
        final ResourceConfig application = new ResourceConfig()
                .packages("resources")
                .register(JacksonFeature.class);

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
            System.out.println("Could not start server");
            e.printStackTrace();
        } finally {
            jettyServer.destroy();
        }
    }
}
