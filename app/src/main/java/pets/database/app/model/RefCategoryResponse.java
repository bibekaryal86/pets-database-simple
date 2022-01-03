package pets.database.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefCategoryResponse implements Serializable {
    private List<RefCategory> refCategories;
    private Long deleteCount;
    private Status status;
}
