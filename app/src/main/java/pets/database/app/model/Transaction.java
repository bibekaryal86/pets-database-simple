package pets.database.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonId;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction implements Serializable {
    @BsonId
    private String id;
    private String description;
    private Account account;
    private Account trfAccount;
    private RefTransactionType refTransactionType;
    private RefCategory refCategory;
    private RefMerchant refMerchant;
    private User user;
    private String date;
    private BigDecimal amount;
    private Boolean regular;
    private Boolean necessary;
    private String creationDate;
    private String lastModified;
}
