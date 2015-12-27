package org.cbcdevotional.devserver;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.servlet.ServletHandler;

public class DevServer
{
  public DevServer()
  {
  }

  public void run()
    throws Exception
  {
    Server server = new Server(8188);

    // Create the ServletHandler that will handle api requests
    ServletHandler servletHandler = new ServletHandler();
    servletHandler.addServletWithMapping(DevApiServlet.class, "/api/1.0/*");

    // Create the ResourceHandler that will take care of static assets
    ResourceHandler resourceHandler = new ResourceHandler();
    resourceHandler.setDirectoriesListed(true);
    resourceHandler.setWelcomeFiles(new String[] { "index.html" });
    resourceHandler.setResourceBase("site");
 
    // Set the server to first search for static assets, then the
    // servlet
    HandlerList handlers = new HandlerList();
    handlers.setHandlers(new Handler[] {
        resourceHandler,
        servletHandler,
        new DefaultHandler()
      });

    // Run everything through gzip
    GzipHandler gzipHandler = new GzipHandler();
    gzipHandler.setHandler(handlers);

    server.setHandler(gzipHandler);

    server.start();
    server.join();
  }
  
  public static void main(String[] args)
    throws Exception
  {
    DevServer ds = new DevServer();
    ds.run();
  }
}
