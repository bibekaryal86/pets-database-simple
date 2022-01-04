package pets.database.app.service;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import pets.database.app.model.*;
import pets.database.app.repository.UserDao;
import pets.database.app.util.Util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserService {

    private static final String ERROR_RETRIEVING_USER = "Error Retrieving User, Please Try Again!!!";
    private static final String ERROR_SAVING_USER = "Error Saving User, Please Try Again!!!";
    private static final String ERROR_UPDATING_USER = "Error Updating User, Please Try Again!!!";
    private static final String ERROR_DELETING_USER = "Error Deleting User, Please Try Again!!!";

    private static User convertDtoToObject(UserDto userDto) {
        return User.builder()
                .id(userDto.getId() == null ? null : userDto.getId().toString())
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .streetAddress(userDto.getStreetAddress())
                .city(userDto.getCity())
                .state(userDto.getState())
                .zipcode(userDto.getZipcode())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .status(userDto.getStatus())
                .creationDate(userDto.getCreationDate())
                .lastModified(userDto.getLastModified())
                .build();
    }

    private static UserDto convertObjectToDto(UserRequest userRequest) {
        return UserDto.builder()
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .streetAddress(userRequest.getStreetAddress())
                .city(userRequest.getCity())
                .state(userRequest.getState())
                .zipcode(userRequest.getZipcode())
                .email(userRequest.getEmail())
                .phone(userRequest.getPhone())
                .status(userRequest.getStatus())
                .build();
    }

    public static UserResponse getUserByUsername(String username) {
        log.info("Before Get User By User Name: {}", username);
        User user = null;
        Status status = null;

        try {
            UserDto userDto = UserDao.findUserByUsername(username);
            if (userDto != null) {
                user = convertDtoToObject(userDto);
            }
        } catch (Exception ex) {
            log.error("Get User By User Name: {}", username, ex);
            status = Status.builder()
                    .errMsg(ERROR_RETRIEVING_USER)
                    .message(ex.toString())
                    .build();
        }

        log.info("After Get User By User Name: {} | {}", username, user);
        return UserResponse.builder()
                .users(user == null ? Collections.emptyList() : List.of(user))
                .status(status)
                .build();
    }

    public static UserResponse saveNewUser(UserRequest userRequest) {
        log.info("Before Save New User: {}", userRequest);
        User user = null;
        Status status = null;

        try {
            UserDto userDto = convertObjectToDto(userRequest);
            userDto.setCreationDate(LocalDate.now().toString());
            userDto.setLastModified(LocalDateTime.now().toString());

            String insertedId = UserDao.saveNewUser(userDto);
            if (Util.hasText(insertedId)) {
                user = convertDtoToObject(userDto);
                user.setId(insertedId);
            } else {
                status = Status.builder()
                        .errMsg(ERROR_SAVING_USER)
                        .build();
            }
        } catch (Exception ex) {
            log.error("Save New User: {}", userRequest, ex);
            status = Status.builder()
                    .errMsg(ERROR_SAVING_USER)
                    .message(ex.toString())
                    .build();
        }

        log.info("After Save New User: {}", user);
        return UserResponse.builder()
                .users(user == null ? Collections.emptyList() : List.of(user))
                .status(status)
                .build();
    }

    public static UserResponse updateUserById(String id, UserRequest userRequest) {
        log.info("Before Update User By Id: {} | {}", id, userRequest);
        UserResponse userResponse;
        Status status;

        try {
            Bson filter = Filters.eq("_id", new ObjectId(id));
            Bson updates = Updates.combine(List.of(
                    Updates.set("password", userRequest.getPassword()),
                    Updates.set("first_name", userRequest.getFirstName()),
                    Updates.set("last_name", userRequest.getLastName()),
                    Updates.set("street_address", userRequest.getStreetAddress()),
                    Updates.set("city", userRequest.getCity()),
                    Updates.set("state", userRequest.getState()),
                    Updates.set("zip_code", userRequest.getZipcode()),
                    Updates.set("phone", userRequest.getPhone()),
                    Updates.set("email", userRequest.getEmail()),
                    Updates.set("status", userRequest.getStatus()),
                    Updates.currentTimestamp("last_modified")));

            long modifiedCount = UserDao.updateUserById(filter, updates);

            if (modifiedCount > 0) {
                userResponse = getUserByUsername(userRequest.getUsername());
            } else {
                status = Status.builder()
                        .errMsg(ERROR_UPDATING_USER)
                        .build();
                userResponse = UserResponse.builder()
                        .users(Collections.emptyList())
                        .status(status)
                        .build();
            }
        } catch (Exception ex) {
            log.error("Update User By Id: {} | {}", id, userRequest, ex);
            status = Status.builder()
                    .errMsg(ERROR_UPDATING_USER)
                    .message(ex.toString())
                    .build();
            userResponse = UserResponse.builder()
                    .users(Collections.emptyList())
                    .status(status)
                    .build();
        }

        log.info("After Update User By Id: {} | user: {}", id, userResponse);
        return userResponse;
    }

    public static UserResponse deleteUserById(String id) {
        log.info("Before Delete User By Id: {}", id);
        long deleteCount = 0;
        Status status = null;

        try {
            Bson filter = Filters.eq("_id", new ObjectId(id));
            deleteCount = UserDao.deleteUserById(filter);
        } catch (Exception ex) {
            log.error("Delete User By Id: {}", id, ex);
            status = Status.builder()
                    .errMsg(ERROR_DELETING_USER)
                    .message(ex.toString())
                    .build();
        }

        log.info("After Delete User By Id: {} | deleteCount: {}", id, deleteCount);
        return UserResponse.builder()
                .users(Collections.emptyList())
                .deleteCount(deleteCount)
                .status(status)
                .build();
    }
}
