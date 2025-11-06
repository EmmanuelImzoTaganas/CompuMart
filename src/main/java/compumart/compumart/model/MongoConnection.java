package compumart.compumart.model;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {
    private static final String CONNECTION_STRING = "mongodb+srv://eitaganas_db_user:syWgrwfcXryC0DxO@slipknotcuster.a2xm1cy.mongodb.net/?retryWrites=true&w=majority&appName=SlipknotCuster";

    private static final String DATABASE_NAME = "User_Database_CompuMart";
    private static MongoClient mongoClient = null;

    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            ConnectionString connString = new ConnectionString(CONNECTION_STRING);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connString)
                    .build();

            mongoClient = MongoClients.create(settings);
        }
        return mongoClient.getDatabase(DATABASE_NAME);
    }
}
