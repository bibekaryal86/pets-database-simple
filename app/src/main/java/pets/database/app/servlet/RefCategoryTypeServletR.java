package pets.database.app.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pets.database.app.model.RefCategoryTypeResponse;
import pets.database.app.service.RefTypesService;

import java.io.IOException;

import static pets.database.app.util.Util.getGson;

public class RefCategoryTypeServletR extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        RefCategoryTypeResponse refCategoryTypeResponse = new RefTypesService().getAllRefCategoryTypes();

        if (refCategoryTypeResponse.getStatus() == null) {
            response.setStatus(200);
        } else {
            response.setStatus(500);
        }

        response.getWriter().print(getGson().toJson(refCategoryTypeResponse));
    }
}
