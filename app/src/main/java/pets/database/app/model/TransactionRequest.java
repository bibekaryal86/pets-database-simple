package pets.database.app.model;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest implements Serializable {
    private String description;
    @NonNull
    private String accountId;
    private String trfAccountId;
    @NonNull
    private String typeId;
    @NonNull
    private String categoryId;
    @NonNull
    private String merchantId;
    @NonNull
    private String username;
    @NonNull
    private String date;
    @NonNull
    private BigDecimal amount;
    @NonNull
    private Boolean regular;
    @NonNull
    private Boolean necessary;
}
