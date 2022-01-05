package pets.database.app.util;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;

import static pets.database.app.util.Util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MongoDbUtil {

    private static String dbName = null;
    private static String dbUsr = null;
    private static String dbPwd = null;

    private static void initMongo() {
        if (dbName == null || dbUsr == null || dbPwd == null) {
            dbName = getSystemEnvProperty(MONGODB_ACC_NAME);
            dbUsr = getSystemEnvProperty(MONGODB_USR_NAME);
            dbPwd = getSystemEnvProperty(MONGODB_USR_PWD);
        }
    }

    public static MongoClientSettings getMongoClientSettings() {
        initMongo();

        return MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(String.format(MONGODB_URI, dbUsr, dbPwd, dbName)))
                .codecRegistry(CodecRegistries.fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        CodecRegistries.fromProviders(PojoCodecProvider.builder()
                                .automatic(true)
                                .build())))
                .build();
    }

    public static MongoCollection<?> getMongoCollection(MongoClient mongoClient, String collectionName, Class<?> clazz) {
        return mongoClient.getDatabase(dbName).getCollection(collectionName, clazz);
    }
}
