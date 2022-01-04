package pets.database.app.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pets.database.app.model.*;
import pets.database.app.service.AccountService;
import pets.database.app.util.Util;

import java.io.IOException;

public class AccountServletUD extends HttpServlet {

    private void doGetPutAndDelete(HttpServletRequest request, HttpServletResponse response,
                                   boolean isGet, boolean isDelete) throws IOException {
        AccountResponse accountResponse;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        String id = Util.getRequestPathParameter(request);

        if (Util.hasText(id)) {
            if (isGet) {
                accountResponse = AccountService.getAccountById(id);
            } else if (isDelete) {
                accountResponse = AccountService.deleteAccountById(id);
            } else {
                AccountRequest accountRequest = (AccountRequest) Util.getRequestBody(request, AccountRequest.class);

                if (accountRequest == null) {
                    accountResponse = AccountResponse.builder()
                            .status(Status.builder()
                                    .errMsg("Error Updating Account! Invalid Request!! Please Try Again!!!")
                                    .build())
                            .build();
                } else {
                    accountResponse = AccountService.updateAccountById(id, accountRequest);
                }
            }

            if (accountResponse.getStatus() == null) {
                response.setStatus(200);
            } else {
                response.setStatus(500);
            }
        } else {
            response.setStatus(400);
            accountResponse = AccountResponse.builder()
                    .status(Status.builder()
                            .errMsg("Error Updating Ref Merchant! Invalid Id!! Please Try Again!!!")
                            .build())
                    .build();
        }

        response.getWriter().print(Util.getGson().toJson(accountResponse));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGetPutAndDelete(request, response, true, false);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
       doGetPutAndDelete(request, response, false, false);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGetPutAndDelete(request, response, false, true);
    }
}