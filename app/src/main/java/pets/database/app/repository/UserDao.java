package pets.database.app.repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import pets.database.app.model.UserDto;
import pets.database.app.util.MongoDbUtil;

import java.util.Objects;

public class UserDao {

    private static final String USER_DETAILS = "user_details";

    private MongoCollection<UserDto> getMongoCollection(MongoClient mongoClient) {
        return (MongoCollection<UserDto>) MongoDbUtil.getMongoCollection(mongoClient, USER_DETAILS, UserDto.class);
    }

    public UserDto findUserByUsername(String username) {
        try (MongoClient mongoClient = MongoClients.create(MongoDbUtil.getMongoClientSettings())) {
            return getMongoCollection(mongoClient).find(Filters.eq("username", username), UserDto.class).first();
        }
    }

    public String saveNewUser(UserDto userDto) {
        try (MongoClient mongoClient = MongoClients.create(MongoDbUtil.getMongoClientSettings())) {
            return Objects.requireNonNull(getMongoCollection(mongoClient).insertOne(userDto).getInsertedId())
                    .asObjectId().getValue().toString();
        }
    }

    public long updateUserById(Bson filter, Bson updates) {
        try (MongoClient mongoClient = MongoClients.create(MongoDbUtil.getMongoClientSettings())) {
            return getMongoCollection(mongoClient).updateOne(filter, updates).getModifiedCount();
        }
    }

    public long deleteUserById(Bson filter) {
        try (MongoClient mongoClient = MongoClients.create(MongoDbUtil.getMongoClientSettings())) {
            return getMongoCollection(mongoClient).deleteOne(filter).getDeletedCount();
        }
    }
}
