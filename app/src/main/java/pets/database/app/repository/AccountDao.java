package pets.database.app.repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import pets.database.app.model.AccountDto;
import pets.database.app.util.MongoDbUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AccountDao {

    private static final String ACCOUNT_DETAILS = "account_details";

    private MongoCollection<AccountDto> getMongoCollection(MongoClient mongoClient) {
        return (MongoCollection<AccountDto>) MongoDbUtil.getMongoCollection(mongoClient, ACCOUNT_DETAILS, AccountDto.class);
    }

    public List<AccountDto> getAllAccountsByUsername(String username) {
        List<AccountDto> accountDtoList = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(MongoDbUtil.getMongoClientSettings())) {
            FindIterable<AccountDto> findIterable = getMongoCollection(mongoClient)
                    .find(Filters.eq("user.username", username))
                    .sort(Sorts.ascending("description"));
            findIterable.forEach(accountDtoList::add);
        }
        return accountDtoList;
    }

    public AccountDto getAccountById(String id) {
        try (MongoClient mongoClient = MongoClients.create(MongoDbUtil.getMongoClientSettings())) {
            return getMongoCollection(mongoClient).find(Filters.eq("_id", new ObjectId(id))).first();
        }
    }

    public String saveNewAccount(AccountDto accountDto) {
        try (MongoClient mongoClient = MongoClients.create(MongoDbUtil.getMongoClientSettings())) {
            return Objects.requireNonNull(getMongoCollection(mongoClient).insertOne(accountDto).getInsertedId())
                    .asObjectId().getValue().toString();
        }
    }

    public long updateAccountById(Bson filter, Bson updates) {
        try (MongoClient mongoClient = MongoClients.create(MongoDbUtil.getMongoClientSettings())) {
            return getMongoCollection(mongoClient).updateOne(filter, updates).getModifiedCount();
        }
    }

    public long deleteAccountById(Bson filter) {
        try (MongoClient mongoClient = MongoClients.create(MongoDbUtil.getMongoClientSettings())) {
            return getMongoCollection(mongoClient).deleteOne(filter).getDeletedCount();
        }
    }
}
