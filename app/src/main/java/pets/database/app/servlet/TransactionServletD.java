package pets.database.app.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pets.database.app.model.Status;
import pets.database.app.model.TransactionResponse;
import pets.database.app.service.TransactionService;

import java.io.IOException;

import static pets.database.app.util.Util.getGson;
import static pets.database.app.util.Util.getRequestPathParameter;
import static pets.database.app.util.Util.hasText;

public class TransactionServletD extends HttpServlet {

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TransactionResponse transactionResponse;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        String accountId = getRequestPathParameter(request);

        if (hasText(accountId)) {
            transactionResponse = new TransactionService().deleteTransactionByAccountId(accountId);

            if (transactionResponse.getStatus() == null) {
                response.setStatus(200);
            } else {
                response.setStatus(500);
            }
        } else {
            response.setStatus(400);
            transactionResponse = TransactionResponse.builder()
                    .status(Status.builder()
                            .errMsg("Error Deleting Transactions! Invalid Account Id!! Please Try Again!!!")
                            .build())
                    .build();
        }

        response.getWriter().print(getGson().toJson(transactionResponse));
    }
}
