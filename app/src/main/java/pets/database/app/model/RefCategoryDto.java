package pets.database.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefCategoryDto implements Serializable {
    @BsonId
    private ObjectId id;
    private String description;
    private RefCategoryTypeDto refCategoryType;
    private String creationDate;
    private String lastModified;
}
