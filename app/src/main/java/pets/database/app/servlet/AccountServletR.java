package pets.database.app.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pets.database.app.model.AccountResponse;
import pets.database.app.model.Status;
import pets.database.app.service.AccountService;
import pets.database.app.util.Util;

import java.io.IOException;

public class AccountServletR extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AccountResponse accountResponse;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        String username = Util.getRequestPathParameter(request);

        if (Util.hasText(username)) {
            accountResponse = AccountService.getAllAccountsByUsername(username);

            if (accountResponse.getStatus() == null) {
                response.setStatus(200);
            } else {
                response.setStatus(500);
            }
        } else {
            response.setStatus(400);
            accountResponse = AccountResponse.builder()
                    .status(Status.builder()
                            .errMsg(String.format("Error Retrieving Accounts by Invalid Username: %s",
                                    username))
                            .build())
                    .build();
        }

        response.getWriter().print(Util.getGson().toJson(accountResponse));
    }
}