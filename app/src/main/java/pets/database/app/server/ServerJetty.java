package pets.database.app.server;

import jakarta.servlet.DispatcherType;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import pets.database.app.filter.ServletFilter;
import pets.database.app.servlet.UserServletC;
import pets.database.app.servlet.UserServletR;
import pets.database.app.servlet.UserServletUD;
import pets.database.app.util.Util;
import pets.database.app.servlet.AppPing;

import java.util.EnumSet;

@Slf4j
public class ServerJetty {

    public void start() throws Exception {
        log.info("Start Jetty Server Initialization!!!");

        QueuedThreadPool threadPool = new QueuedThreadPool(Util.SERVER_MAX_THREADS, Util.SERVER_MIN_THREADS, Util.SERVER_IDLE_TIMEOUT);
        Server server = new Server(threadPool);

        try (ServerConnector connector = new ServerConnector(server)) {
            String port = Util.getSystemEnvProperty(Util.SERVER_PORT);
            connector.setPort(port == null ? 8080 : Integer.parseInt(port));
            server.setConnectors(new Connector[]{connector});
        }

        server.setHandler(getServletHandler());
        server.start();
        log.info("Finish Jetty Server Initialization!!!");
    }

    private ServletHandler getServletHandler() {
        ServletHandler servletHandler = new ServletHandler();
        servletHandler.addFilterWithMapping(ServletFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));

        servletHandler.addServletWithMapping(AppPing.class, Util.CONTEXT_PATH + "/tests/ping");

        // User CRUD
        servletHandler.addServletWithMapping(UserServletC.class, Util.CONTEXT_PATH + "/users/user");
        servletHandler.addServletWithMapping(UserServletR.class, Util.CONTEXT_PATH + "/users/user/username/*");
        servletHandler.addServletWithMapping(UserServletUD.class, Util.CONTEXT_PATH + "/users/user/id/*");

        return servletHandler;
    }
}