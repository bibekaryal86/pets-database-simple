package pets.database.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto implements Serializable {
    @BsonId
    private ObjectId id;
    private RefAccountTypeDto refAccountType;
    private RefBankDto refBank;
    private String description;
    private UserDto user;
    @BsonProperty(value = "opening_balance")
    private String openingBalance;
    private String status;
    private String creationDate;
    private String lastModified;
}
