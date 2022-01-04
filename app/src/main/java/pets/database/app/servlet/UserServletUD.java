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

public class UserServletUD extends HttpServlet {

    private void doPutAndDelete(HttpServletRequest request, HttpServletResponse response, boolean isDelete) throws IOException {
        UserResponse userResponse;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        String id = Util.getRequestPathParameter(request);

        if (Util.hasText(id)) {
            if (isDelete) {
                userResponse = UserService.deleteUserById(id);
            } else {
                UserRequest userRequest = (UserRequest) Util.getRequestBody(request, UserRequest.class);

                if (userRequest == null) {
                    userResponse = UserResponse.builder()
                            .status(Status.builder()
                                    .errMsg("Error Updating User! Invalid Request!! Please Try Again!!!")
                                    .build())
                            .build();
                } else {
                    userResponse = UserService.updateUserById(id, userRequest);
                }
            }

            if (userResponse.getStatus() == null) {
                response.setStatus(200);
            } else {
                response.setStatus(500);
            }
        } else {
            response.setStatus(400);
            userResponse = UserResponse.builder()
                    .status(Status.builder()
                            .errMsg("Error Updating User! Invalid Id!! Please Try Again!!!")
                            .build())
                    .build();
        }

        response.getWriter().print(Util.getGson().toJson(userResponse));
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
       doPutAndDelete(request, response, false);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPutAndDelete(request, response, true);
    }
}
