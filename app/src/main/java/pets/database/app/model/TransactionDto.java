package pets.database.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import java.io.Serializable;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto implements Serializable {
    @BsonId
    private ObjectId id;
    private String description;
    private AccountDto account;
    private AccountDto trfAccount;
    private RefTransactionTypeDto refTransactionType;
    private RefCategoryDto refCategory;
    private RefMerchantDto refMerchant;
    private UserDto user;
    private String date;
    private String amount;
    private Boolean regular;
    private Boolean necessary;
    private String creationDate;
    private String lastModified;
}
