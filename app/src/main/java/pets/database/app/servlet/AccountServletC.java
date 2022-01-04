package pets.database.app.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pets.database.app.model.AccountRequest;
import pets.database.app.model.AccountResponse;
import pets.database.app.model.Status;
import pets.database.app.service.AccountService;
import pets.database.app.util.Util;

import java.io.IOException;

public class AccountServletC extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AccountResponse accountResponse;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        AccountRequest accountRequest = (AccountRequest) Util.getRequestBody(request, AccountRequest.class);

        if (accountRequest != null) {
            accountResponse = AccountService.saveNewAccount(accountRequest);

            if (accountResponse.getStatus() == null) {
                response.setStatus(200);
            } else {
                response.setStatus(500);
            }
        } else {
            response.setStatus(400);
            accountResponse = AccountResponse.builder()
                    .status(Status.builder()
                            .errMsg("Error Saving Account! Invalid Request!! Please Try Again!!!")
                            .build())
                    .build();
        }

        response.getWriter().print(Util.getGson().toJson(accountResponse));
    }
}
