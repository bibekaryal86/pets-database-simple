package pets.database.app.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pets.database.app.model.RefMerchantRequest;
import pets.database.app.model.RefMerchantResponse;
import pets.database.app.model.Status;
import pets.database.app.service.RefMerchantService;
import pets.database.app.util.Util;

import java.io.IOException;

public class RefMerchantServletC extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RefMerchantResponse refMerchantResponse;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        RefMerchantRequest refMerchantRequest = (RefMerchantRequest) Util.getRequestBody(request, RefMerchantRequest.class);

        if (refMerchantRequest != null) {
            refMerchantResponse = RefMerchantService.saveNewRefMerchant(refMerchantRequest);

            if (refMerchantResponse.getStatus() == null) {
                response.setStatus(200);
            } else {
                response.setStatus(500);
            }
        } else {
            response.setStatus(400);
            refMerchantResponse = RefMerchantResponse.builder()
                    .status(Status.builder()
                            .errMsg("Error Saving Ref Merchant! Invalid Request!! Please Try Again!!!")
                            .build())
                    .build();
        }

        response.getWriter().print(Util.getGson().toJson(refMerchantResponse));
    }
}
