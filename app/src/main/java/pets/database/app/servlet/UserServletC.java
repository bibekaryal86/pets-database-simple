package pets.database.app.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pets.database.app.model.Status;
import pets.database.app.model.UserRequest;
import pets.database.app.model.UserResponse;
import pets.database.app.service.UserService;
import pets.database.app.util.Util;

import java.io.IOException;

public class UserServletC extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserResponse userResponse;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        UserRequest userRequest = (UserRequest) Util.getRequestBody(request, UserRequest.class);

        if (userRequest != null) {
            userResponse = new UserService().saveNewUser(userRequest);

            if (userResponse.getStatus() == null) {
                response.setStatus(200);
            } else {
                response.setStatus(500);
            }
        } else {
            response.setStatus(400);
            userResponse = UserResponse.builder()
                    .status(Status.builder()
                            .errMsg("Error Saving User! Invalid Request!! Please Try Again!!!")
                            .build())
                    .build();
        }

        response.getWriter().print(Util.getGson().toJson(userResponse));
    }
}
