package pets.database.app.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pets.database.app.model.Status;
import pets.database.app.model.UserRequest;
import pets.database.app.model.UserResponse;
import pets.database.app.service.UserService;

import java.io.IOException;

import static pets.database.app.util.Util.getGson;
import static pets.database.app.util.Util.getRequestBody;
import static pets.database.app.util.Util.getRequestPathParameter;
import static pets.database.app.util.Util.hasText;

public class UserServletUD extends HttpServlet {

    private void doPutAndDelete(HttpServletRequest request, HttpServletResponse response, boolean isDelete) throws IOException {
        UserResponse userResponse;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        String id = getRequestPathParameter(request);

        if (hasText(id)) {
            if (isDelete) {
                userResponse = new UserService().deleteUserById(id);
            } else {
                UserRequest userRequest = (UserRequest) getRequestBody(request, UserRequest.class);

                if (userRequest == null) {
                    userResponse = UserResponse.builder()
                            .status(Status.builder()
                                    .errMsg("Error Updating User! Invalid Request!! Please Try Again!!!")
                                    .build())
                            .build();
                } else {
                    userResponse = new UserService().updateUserById(id, userRequest);
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

        response.getWriter().print(getGson().toJson(userResponse));
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
