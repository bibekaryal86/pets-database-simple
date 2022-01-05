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

public class RefMerchantServletUD extends HttpServlet {

    private void doGetPutAndDelete(HttpServletRequest request, HttpServletResponse response,
                                   boolean isGet, boolean isDelete) throws IOException {
        RefMerchantResponse refMerchantResponse;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        String id = Util.getRequestPathParameter(request);

        if (Util.hasText(id)) {
            if (isGet) {
                refMerchantResponse = new RefMerchantService().getRefMerchantById(id);
            } else if (isDelete) {
                refMerchantResponse = new RefMerchantService().deleteRefMerchantById(id);
            } else {
                RefMerchantRequest refMerchantRequest = (RefMerchantRequest) Util.getRequestBody(request, RefMerchantRequest.class);

                if (refMerchantRequest == null) {
                    refMerchantResponse = RefMerchantResponse.builder()
                            .status(Status.builder()
                                    .errMsg("Error Updating Ref Merchant! Invalid Request!! Please Try Again!!!")
                                    .build())
                            .build();
                } else {
                    refMerchantResponse = new RefMerchantService().updateRefMerchantById(id, refMerchantRequest);
                }
            }

            if (refMerchantResponse.getStatus() == null) {
                response.setStatus(200);
            } else {
                response.setStatus(500);
            }
        } else {
            response.setStatus(400);
            refMerchantResponse = RefMerchantResponse.builder()
                    .status(Status.builder()
                            .errMsg("Error Processing Request! Invalid Id!! Please Try Again!!!")
                            .build())
                    .build();
        }

        response.getWriter().print(Util.getGson().toJson(refMerchantResponse));
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
