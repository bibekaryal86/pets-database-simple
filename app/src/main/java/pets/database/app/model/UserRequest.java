package pets.database.app.model;

import lombok.*;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest implements Serializable {
    @NonNull
    private String username;
    @NonNull
    @ToString.Exclude private String password;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    private String streetAddress;
    private String city;
    private String state;
    private String zipcode;
    @NonNull
    private String email;
    @NonNull
    private String phone;
    @NonNull
    private String status;
}