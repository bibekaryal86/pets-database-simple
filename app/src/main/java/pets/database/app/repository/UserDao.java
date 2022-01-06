package pets.database.app.repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import pets.database.app.model.UserDto;

import java.util.Objects;

import static pets.database.app.util.MongoDbUtil.getMongoClientSettings;
import static pets.database.app.util.MongoDbUtil.getMongoCollection;

public class UserDao {

    private static final String USER_DETAILS = "user_details";

    private MongoCollection<UserDto> getMongoCollectionUser(MongoClient mongoClient) {
        return (MongoCollection<UserDto>) getMongoCollection(mongoClient, USER_DETAILS, UserDto.class);
    }

    public UserDto findUserByUsername(String username) {
        try (MongoClient mongoClient = MongoClients.create(getMongoClientSettings())) {
            return getMongoCollectionUser(mongoClient).find(Filters.eq("username", username), UserDto.class).first();
        }
    }

    public String saveNewUser(UserDto userDto) {
        try (MongoClient mongoClient = MongoClients.create(getMongoClientSettings())) {
            return Objects.requireNonNull(getMongoCollectionUser(mongoClient).insertOne(userDto).getInsertedId())
                    .asObjectId().getValue().toString();
        }
    }

    public long updateUserById(Bson filter, Bson updates) {
        try (MongoClient mongoClient = MongoClients.create(getMongoClientSettings())) {
            return getMongoCollectionUser(mongoClient).updateOne(filter, updates).getModifiedCount();
        }
    }

    public long deleteUserById(Bson filter) {
        try (MongoClient mongoClient = MongoClients.create(getMongoClientSettings())) {
            return getMongoCollectionUser(mongoClient).deleteOne(filter).getDeletedCount();
        }
    }
}
