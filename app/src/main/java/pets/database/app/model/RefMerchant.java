package pets.database.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonId;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefMerchant implements Serializable {
    @BsonId
    private String id;
    private String description;
    private User user;
    private Boolean usedInUserTransaction;
    private String creationDate;
    private String lastModified;
}
