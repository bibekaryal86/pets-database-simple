package pets.database.app.server;

import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import pets.database.app.filter.ServletFilter;
import pets.database.app.servlet.*;
import pets.database.app.util.Util;

import java.util.EnumSet;

public class ServerJetty {

    public void start() throws Exception {
        QueuedThreadPool threadPool = new QueuedThreadPool(Util.SERVER_MAX_THREADS, Util.SERVER_MIN_THREADS, Util.SERVER_IDLE_TIMEOUT);
        Server server = new Server(threadPool);

        try (ServerConnector connector = new ServerConnector(server)) {
            String port = Util.getSystemEnvProperty(Util.SERVER_PORT);
            connector.setPort(port == null ? 8080 : Integer.parseInt(port));
            server.setConnectors(new Connector[]{connector});
        }

        server.setHandler(getServletHandler());
        server.start();
    }

    private ServletHandler getServletHandler() {
        ServletHandler servletHandler = new ServletHandler();
        servletHandler.addFilterWithMapping(ServletFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));

        servletHandler.addServletWithMapping(AppPing.class, Util.CONTEXT_PATH + "/tests/ping");

        // User CRUD
        servletHandler.addServletWithMapping(UserServletC.class, Util.CONTEXT_PATH + "/users/user");
        servletHandler.addServletWithMapping(UserServletR.class, Util.CONTEXT_PATH + "/users/user/username/*");
        servletHandler.addServletWithMapping(UserServletUD.class, Util.CONTEXT_PATH + "/users/user/id/*");

        // Ref Types R
        servletHandler.addServletWithMapping(RefAccountTypeServletR.class, Util.CONTEXT_PATH + "/refaccounttypes/refaccounttype");
        servletHandler.addServletWithMapping(RefBankServletR.class, Util.CONTEXT_PATH + "/refbanks/refbank");
        servletHandler.addServletWithMapping(RefCategoryServletR.class, Util.CONTEXT_PATH + "/refcategories/refcategory");
        servletHandler.addServletWithMapping(RefCategoryTypeServletR.class, Util.CONTEXT_PATH + "/refcategorytypes/refcategorytype");
        servletHandler.addServletWithMapping(RefTransactionTypeServletR.class, Util.CONTEXT_PATH + "/reftransactiontypes/reftransactiontype");

        // Ref Merchant CRUD (UD also has an R)
        servletHandler.addServletWithMapping(RefMerchantServletC.class, Util.CONTEXT_PATH + "/refmerchants/refmerchant");
        servletHandler.addServletWithMapping(RefMerchantServletR.class, Util.CONTEXT_PATH + "/refmerchants/refmerchant/user/*");
        servletHandler.addServletWithMapping(RefMerchantServletUD.class, Util.CONTEXT_PATH + "/refmerchants/refmerchant/id/*");

        // Account CRUD (UD also has an R)
        servletHandler.addServletWithMapping(AccountServletC.class, Util.CONTEXT_PATH + "/accounts/account");
        servletHandler.addServletWithMapping(AccountServletR.class, Util.CONTEXT_PATH + "/accounts/account/user/*");
        servletHandler.addServletWithMapping(AccountServletUD.class, Util.CONTEXT_PATH + "/accounts/account/id/*");

        // Transaction CRUD (UD also has an R)
        servletHandler.addServletWithMapping(TransactionServletC.class, Util.CONTEXT_PATH + "/transactions/transaction");
        servletHandler.addServletWithMapping(TransactionServletR.class, Util.CONTEXT_PATH + "/transactions/transaction/user/*");
        servletHandler.addServletWithMapping(TransactionServletUD.class, Util.CONTEXT_PATH + "/transactions/transaction/id/*");
        // Transaction extra D
        servletHandler.addServletWithMapping(TransactionServletD.class, Util.CONTEXT_PATH + "/transactions/transaction/accountid/*");

        return servletHandler;
    }
}
