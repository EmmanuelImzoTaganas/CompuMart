package compumart.compumart.utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class MongoDBConnectionManager {

    private static final String CONNECTION_STRING = "mongodb+srv://davidvallenas2003_db_user:S2f14AHEnPTjxQlM@goofygoobers.n0hpolu.mongodb.net/";
    private static MongoClient mongoClient = null;
    private static final Map<String, MongoDatabase> databases = new HashMap<>();

    private static MongoClient getClient() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(CONNECTION_STRING);
        }
        return mongoClient;
    }

    public static MongoDatabase getDatabase(String dbName) {
        if (!databases.containsKey(dbName)) {
            databases.put(dbName, getClient().getDatabase(dbName));
        }
        return databases.get(dbName);
    }
}