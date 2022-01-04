package pets.database.app.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pets.database.app.model.RefBankResponse;
import pets.database.app.service.RefTypesService;
import pets.database.app.util.Util;

import java.io.IOException;

public class RefBankR extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        RefBankResponse refBankResponse = RefTypesService.getAllRefBanks();

        if (refBankResponse.getStatus() == null) {
            response.setStatus(200);
        } else {
            response.setStatus(500);
        }

        response.getWriter().print(Util.getGson().toJson(refBankResponse));
    }
}
