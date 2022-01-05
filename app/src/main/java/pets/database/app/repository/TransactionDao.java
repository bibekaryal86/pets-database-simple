package pets.database.app.repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import pets.database.app.model.TransactionDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static pets.database.app.util.MongoDbUtil.getMongoClientSettings;
import static pets.database.app.util.MongoDbUtil.getMongoCollection;

public class TransactionDao {

    private static final String TRANSACTION_DETAILS = "transaction_details";

    private MongoCollection<TransactionDto> getMongoCollectionTransaction(MongoClient mongoClient) {
        return (MongoCollection<TransactionDto>) getMongoCollection(mongoClient, TRANSACTION_DETAILS, TransactionDto.class);
    }

    public List<TransactionDto> getAllTransactionsByUsername(String username) {
        List<TransactionDto> transactionDtoList = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(getMongoClientSettings())) {
            FindIterable<TransactionDto> findIterable = getMongoCollectionTransaction(mongoClient)
                    .find(Filters.eq("user.username", username))
                    .sort(Sorts.descending("date"));
            findIterable.forEach(transactionDtoList::add);
        }
        return transactionDtoList;
    }

    public TransactionDto getTransactionById(String id) {
        try (MongoClient mongoClient = MongoClients.create(getMongoClientSettings())) {
            return getMongoCollectionTransaction(mongoClient).find(Filters.eq("_id", new ObjectId(id))).first();
        }
    }

    public String saveNewTransaction(TransactionDto transactionDto) {
        try (MongoClient mongoClient = MongoClients.create(getMongoClientSettings())) {
            return Objects.requireNonNull(getMongoCollectionTransaction(mongoClient).insertOne(transactionDto).getInsertedId())
                    .asObjectId().getValue().toString();
        }
    }

    public long updateTransactionById(Bson filter, Bson updates) {
        try (MongoClient mongoClient = MongoClients.create(getMongoClientSettings())) {
            return getMongoCollectionTransaction(mongoClient).updateOne(filter, updates).getModifiedCount();
        }
    }

    public long deleteTransactionById(Bson filter) {
        try (MongoClient mongoClient = MongoClients.create(getMongoClientSettings())) {
            return getMongoCollectionTransaction(mongoClient).deleteOne(filter).getDeletedCount();
        }
    }

    public long deleteTransactionByAccountId(Bson filter) {
        try (MongoClient mongoClient = MongoClients.create(getMongoClientSettings())) {
            return getMongoCollectionTransaction(mongoClient).deleteMany(filter).getDeletedCount();
        }
    }
}
