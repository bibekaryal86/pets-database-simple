package pets.database.app.repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import pets.database.app.model.TransactionDto;
import pets.database.app.util.MongoDbUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionDao {

    private static final String TRANSACTION_DETAILS = "transaction_details";

    private static MongoCollection<TransactionDto> getMongoCollection(MongoClient mongoClient) {
        return (MongoCollection<TransactionDto>) MongoDbUtil.getMongoCollection(mongoClient, TRANSACTION_DETAILS, TransactionDto.class);
    }

    public static List<TransactionDto> getAllTransactionsByUsername(String username) {
        List<TransactionDto> transactionDtoList = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(MongoDbUtil.getMongoClientSettings())) {
            FindIterable<TransactionDto> findIterable = getMongoCollection(mongoClient)
                    .find(Filters.eq("user.username", username))
                    .sort(Sorts.descending("date"));
            findIterable.forEach(transactionDtoList::add);
        }
        return transactionDtoList;
    }

    public static TransactionDto getTransactionById(String id) {
        try (MongoClient mongoClient = MongoClients.create(MongoDbUtil.getMongoClientSettings())) {
            return getMongoCollection(mongoClient).find(Filters.eq("_id", new ObjectId(id))).first();
        }
    }

    public static String saveNewTransaction(TransactionDto transactionDto) {
        try (MongoClient mongoClient = MongoClients.create(MongoDbUtil.getMongoClientSettings())) {
            return Objects.requireNonNull(getMongoCollection(mongoClient).insertOne(transactionDto).getInsertedId())
                    .asObjectId().getValue().toString();
        }
    }

    public static long updateTransactionById(Bson filter, Bson updates) {
        try (MongoClient mongoClient = MongoClients.create(MongoDbUtil.getMongoClientSettings())) {
            return getMongoCollection(mongoClient).updateOne(filter, updates).getModifiedCount();
        }
    }

    public static long deleteTransactionById(Bson filter) {
        try (MongoClient mongoClient = MongoClients.create(MongoDbUtil.getMongoClientSettings())) {
            return getMongoCollection(mongoClient).deleteOne(filter).getDeletedCount();
        }
    }
}
