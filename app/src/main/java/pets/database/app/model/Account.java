package pets.database.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account implements Serializable {
    @BsonId
    private String id;
    private RefAccountType refAccountType;
    private RefBank refBank;
    private String description;
    private User user;
    @BsonProperty(value = "opening_balance")
    private BigDecimal openingBalance;
    private String status;
    private String creationDate;
    private String lastModified;
}
