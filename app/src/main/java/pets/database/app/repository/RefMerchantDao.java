package pets.database.app.repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import pets.database.app.model.RefMerchantDto;
import pets.database.app.util.MongoDbUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RefMerchantDao {

    private static final String REF_MERCHANT_DETAILS = "ref_merchant_details";

    private MongoCollection<RefMerchantDto> getMongoCollection(MongoClient mongoClient) {
        return (MongoCollection<RefMerchantDto>) MongoDbUtil.getMongoCollection(mongoClient, REF_MERCHANT_DETAILS, RefMerchantDto.class);
    }

    public List<RefMerchantDto> getAllRefMerchantsByUsername(String username) {
        List<RefMerchantDto> refMerchantDtoList = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(MongoDbUtil.getMongoClientSettings())) {
            FindIterable<RefMerchantDto> findIterable = getMongoCollection(mongoClient)
                    .find(Filters.eq("user.username", username))
                    .sort(Sorts.ascending("description"));
            findIterable.forEach(refMerchantDtoList::add);
        }
        return refMerchantDtoList;
    }

    public RefMerchantDto getRefMerchantById(String id) {
        try (MongoClient mongoClient = MongoClients.create(MongoDbUtil.getMongoClientSettings())) {
            return getMongoCollection(mongoClient).find(Filters.eq("_id", new ObjectId(id))).first();
        }
    }

    public String saveNewRefMerchant(RefMerchantDto refMerchantDto) {
        try (MongoClient mongoClient = MongoClients.create(MongoDbUtil.getMongoClientSettings())) {
            return Objects.requireNonNull(getMongoCollection(mongoClient).insertOne(refMerchantDto).getInsertedId())
                    .asObjectId().getValue().toString();
        }
    }

    public long updateRefMerchantById(Bson filter, Bson updates) {
        try (MongoClient mongoClient = MongoClients.create(MongoDbUtil.getMongoClientSettings())) {
            return getMongoCollection(mongoClient).updateOne(filter, updates).getModifiedCount();
        }
    }

    public long deleteRefMerchantById(Bson filter) {
        try (MongoClient mongoClient = MongoClients.create(MongoDbUtil.getMongoClientSettings())) {
            return getMongoCollection(mongoClient).deleteOne(filter).getDeletedCount();
        }
    }
}
