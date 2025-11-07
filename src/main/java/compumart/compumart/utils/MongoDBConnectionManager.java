package compumart.compumart.utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnectionManager {

    private static final String CONNECTION_STRING =
            "mongodb+srv://davidvallenas2003_db_user:S2f14AHEnPTjxQlM@goofygoobers.n0hpolu.mongodb.net/";
    private static final String DATABASE_STRING = "CompuMART";

    private static final MongoDBConnectionManager instance = new MongoDBConnectionManager();

    private MongoClient mongoClient;
    private MongoDatabase database;

    private MongoDBConnectionManager() {
        // private constructor for singleton
    }

    public static MongoDBConnectionManager getInstance() {
        return instance;
    }

    public MongoClient getDatabaseClient() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(CONNECTION_STRING);
        }
        return mongoClient;
    }

    public MongoDatabase getDatabase() {
        if (database == null) {
            if (mongoClient == null) {
                mongoClient = MongoClients.create(CONNECTION_STRING);
            }
            database = mongoClient.getDatabase(DATABASE_STRING);
        }
        return database;
    }
}
