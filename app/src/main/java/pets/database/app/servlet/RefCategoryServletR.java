package pets.database.app.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pets.database.app.model.RefCategoryResponse;
import pets.database.app.service.RefTypesService;

import java.io.IOException;

import static pets.database.app.util.Util.getGson;

public class RefCategoryServletR extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        RefCategoryResponse refCategoryResponse = new RefTypesService().getAllRefCategories();

        if (refCategoryResponse.getStatus() == null) {
            response.setStatus(200);
        } else {
            response.setStatus(500);
        }

        response.getWriter().print(getGson().toJson(refCategoryResponse));
    }
}
