package pets.database.app.server;

import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import pets.database.app.filter.ServletFilter;
import pets.database.app.servlet.AccountServletC;
import pets.database.app.servlet.AccountServletR;
import pets.database.app.servlet.AccountServletUD;
import pets.database.app.servlet.AppPing;
import pets.database.app.servlet.RefAccountTypeServletR;
import pets.database.app.servlet.RefBankServletR;
import pets.database.app.servlet.RefCategoryServletR;
import pets.database.app.servlet.RefCategoryTypeServletR;
import pets.database.app.servlet.RefMerchantServletC;
import pets.database.app.servlet.RefMerchantServletR;
import pets.database.app.servlet.RefMerchantServletUD;
import pets.database.app.servlet.RefTransactionTypeServletR;
import pets.database.app.servlet.TransactionServletC;
import pets.database.app.servlet.TransactionServletD;
import pets.database.app.servlet.TransactionServletR;
import pets.database.app.servlet.TransactionServletUD;
import pets.database.app.servlet.UserServletC;
import pets.database.app.servlet.UserServletR;
import pets.database.app.servlet.UserServletUD;

import java.util.EnumSet;

import static pets.database.app.util.Util.CONTEXT_PATH;
import static pets.database.app.util.Util.SERVER_IDLE_TIMEOUT;
import static pets.database.app.util.Util.SERVER_MAX_THREADS;
import static pets.database.app.util.Util.SERVER_MIN_THREADS;
import static pets.database.app.util.Util.SERVER_PORT;
import static pets.database.app.util.Util.getSystemEnvProperty;

public class ServerJetty {

    public void start() throws Exception {
        QueuedThreadPool threadPool = new QueuedThreadPool(SERVER_MAX_THREADS, SERVER_MIN_THREADS, SERVER_IDLE_TIMEOUT);
        Server server = new Server(threadPool);

        try (ServerConnector connector = new ServerConnector(server)) {
            String port = getSystemEnvProperty(SERVER_PORT);
            connector.setPort(port == null ? 8080 : Integer.parseInt(port));
            server.setConnectors(new Connector[]{connector});
        }

        server.setHandler(getServletHandler());
        server.start();
    }

    private ServletHandler getServletHandler() {
        ServletHandler servletHandler = new ServletHandler();
        servletHandler.addFilterWithMapping(ServletFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));

        servletHandler.addServletWithMapping(AppPing.class,  CONTEXT_PATH + "/tests/ping");

        // User CRUD
        servletHandler.addServletWithMapping(UserServletC.class,  CONTEXT_PATH + "/users/user");
        servletHandler.addServletWithMapping(UserServletR.class,  CONTEXT_PATH + "/users/user/username/*");
        servletHandler.addServletWithMapping(UserServletUD.class,  CONTEXT_PATH + "/users/user/id/*");

        // Ref Types R
        servletHandler.addServletWithMapping(RefAccountTypeServletR.class,  CONTEXT_PATH + "/refaccounttypes/refaccounttype");
        servletHandler.addServletWithMapping(RefBankServletR.class,  CONTEXT_PATH + "/refbanks/refbank");
        servletHandler.addServletWithMapping(RefCategoryServletR.class,  CONTEXT_PATH + "/refcategories/refcategory");
        servletHandler.addServletWithMapping(RefCategoryTypeServletR.class,  CONTEXT_PATH + "/refcategorytypes/refcategorytype");
        servletHandler.addServletWithMapping(RefTransactionTypeServletR.class,  CONTEXT_PATH + "/reftransactiontypes/reftransactiontype");

        // Ref Merchant CRUD (UD also has an R)
        servletHandler.addServletWithMapping(RefMerchantServletC.class,  CONTEXT_PATH + "/refmerchants/refmerchant");
        servletHandler.addServletWithMapping(RefMerchantServletR.class,  CONTEXT_PATH + "/refmerchants/refmerchant/user/*");
        servletHandler.addServletWithMapping(RefMerchantServletUD.class,  CONTEXT_PATH + "/refmerchants/refmerchant/id/*");

        // Account CRUD (UD also has an R)
        servletHandler.addServletWithMapping(AccountServletC.class,  CONTEXT_PATH + "/accounts/account");
        servletHandler.addServletWithMapping(AccountServletR.class,  CONTEXT_PATH + "/accounts/account/user/*");
        servletHandler.addServletWithMapping(AccountServletUD.class,  CONTEXT_PATH + "/accounts/account/id/*");

        // Transaction CRUD (UD also has an R)
        servletHandler.addServletWithMapping(TransactionServletC.class,  CONTEXT_PATH + "/transactions/transaction");
        servletHandler.addServletWithMapping(TransactionServletR.class,  CONTEXT_PATH + "/transactions/transaction/user/*");
        servletHandler.addServletWithMapping(TransactionServletUD.class,  CONTEXT_PATH + "/transactions/transaction/id/*");
        // Transaction extra D
        servletHandler.addServletWithMapping(TransactionServletD.class,  CONTEXT_PATH + "/transactions/transaction/accountid/*");

        return servletHandler;
    }
}
