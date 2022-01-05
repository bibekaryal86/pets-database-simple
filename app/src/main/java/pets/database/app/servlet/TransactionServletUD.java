package pets.database.app.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pets.database.app.model.Status;
import pets.database.app.model.TransactionRequest;
import pets.database.app.model.TransactionResponse;
import pets.database.app.service.TransactionService;

import java.io.IOException;

import static pets.database.app.util.Util.*;

public class TransactionServletUD extends HttpServlet {

    private void doGetPutAndDelete(HttpServletRequest request, HttpServletResponse response,
                                   boolean isGet, boolean isDelete) throws IOException {
        TransactionResponse transactionResponse;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        String id = getRequestPathParameter(request);

        if (hasText(id)) {
            if (isGet) {
                transactionResponse = new TransactionService().getTransactionById(id);
            } else if (isDelete) {
                transactionResponse = new TransactionService().deleteTransactionById(id);
            } else {
                TransactionRequest transactionRequest = (TransactionRequest) getRequestBody(request, TransactionRequest.class);

                if (transactionRequest == null) {
                    transactionResponse = TransactionResponse.builder()
                            .status(Status.builder()
                                    .errMsg("Error Updating Transaction! Invalid Request!! Please Try Again!!!")
                                    .build())
                            .build();
                } else {
                    transactionResponse = new TransactionService().updateTransactionById(id, transactionRequest);
                }
            }

            if (transactionResponse.getStatus() == null) {
                response.setStatus(200);
            } else {
                response.setStatus(500);
            }
        } else {
            response.setStatus(400);
            transactionResponse = TransactionResponse.builder()
                    .status(Status.builder()
                            .errMsg("Error Processing Request! Invalid Id!! Please Try Again!!!")
                            .build())
                    .build();
        }

        response.getWriter().print(getGson().toJson(transactionResponse));
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
