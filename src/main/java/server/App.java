package server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ServerProperties;

public class App 
{
    public static void main( String[] args )
    {
 
        Server jettyServer = new Server(8082);
        
        ResourceHandler staticContext = new ResourceHandler();
        staticContext.setDirectoriesListed(true);
        staticContext.setWelcomeFiles(new String[] { "index.html" });
        staticContext.setResourceBase(".");
        
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/resources/*");
        jerseyServlet.setInitOrder(0);
        
        // Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter(ServerProperties.PROVIDER_PACKAGES, "resources");
        
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { staticContext, context});
        jettyServer.setHandler(handlers);
        
       // jettyServer.setHandler(context);
        
        try {
            jettyServer.start();
            jettyServer.join();
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            jettyServer.destroy();
        }
    }
}
