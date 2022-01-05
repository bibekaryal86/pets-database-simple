package pets.database.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    @BsonId
    private ObjectId id;
    private String username;
    @ToString.Exclude
    private String password;
    @BsonProperty(value = "first_name")
    private String firstName;
    @BsonProperty(value = "last_name")
    private String lastName;
    @BsonProperty(value = "street_address")
    private String streetAddress;
    private String city;
    private String state;
    @BsonProperty(value = "zip_code")
    private String zipcode;
    private String email;
    private String phone;
    private String status;
    private String creationDate;
    private String lastModified;
}
