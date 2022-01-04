package pets.database.app.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pets.database.app.model.Status;
import pets.database.app.model.UserResponse;
import pets.database.app.service.UserService;
import pets.database.app.util.Util;

import java.io.IOException;

public class UserServletR extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserResponse userResponse;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        String username = Util.getRequestPathParameter(request);
        String usernameHeader = request.getHeader("user-header");

        if (Util.hasText(username) && Util.hasText(usernameHeader) && username.equals(usernameHeader)) {
            userResponse = UserService.getUserByUsername(username);

            if (userResponse.getStatus() == null) {
                response.setStatus(200);
            } else {
                response.setStatus(500);
            }
        } else {
            response.setStatus(400);
            userResponse = UserResponse.builder()
                    .status(Status.builder()
                            .errMsg(String.format("Error Retrieving User by Invalid Username and/or Header: %s | %s",
                                    username, usernameHeader))
                            .build())
                    .build();
        }

        response.getWriter().print(Util.getGson().toJson(userResponse));
    }
}
