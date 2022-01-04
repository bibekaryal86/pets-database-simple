package pets.database.app.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pets.database.app.model.Status;
import pets.database.app.model.TransactionRequest;
import pets.database.app.model.TransactionResponse;
import pets.database.app.service.TransactionService;
import pets.database.app.util.Util;

import java.io.IOException;

public class TransactionServletC extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TransactionResponse transactionResponse;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        TransactionRequest transactionRequest = (TransactionRequest) Util.getRequestBody(request, TransactionRequest.class);

        if (transactionRequest != null) {
            transactionResponse = TransactionService.saveNewTransaction(transactionRequest);

            if (transactionResponse.getStatus() == null) {
                response.setStatus(200);
            } else {
                response.setStatus(500);
            }
        } else {
            response.setStatus(400);
            transactionResponse = TransactionResponse.builder()
                    .status(Status.builder()
                            .errMsg("Error Saving Transaction! Invalid Request!! Please Try Again!!!")
                            .build())
                    .build();
        }

        response.getWriter().print(Util.getGson().toJson(transactionResponse));
    }
}
