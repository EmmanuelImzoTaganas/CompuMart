package compumart.compumart.model;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {
    private static final String CONNECTION_STRING = "mongodb+srv://eitaganas_db_user:syWgrwfcXryC0DxO@slipknotcuster.a2xm1cy.mongodb.net/\n ";
    private static final String DATABASE_NAME = "User-Details"; // new db NAME

    private static MongoClient mongoClient = null;

    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(CONNECTION_STRING);
        }
        return mongoClient.getDatabase(DATABASE_NAME);
    }
}
