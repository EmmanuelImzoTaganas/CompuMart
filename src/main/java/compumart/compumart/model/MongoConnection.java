package compumart.compumart.model;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

public class MongoConnection {
    private static final String CONNECTION_STRING = "mongodb+srv://eitaganas_db_user:syWgrwfcXryC0DxO@slipknotcuster.a2xm1cy.mongodb.net/?retryWrites=true&w=majority&appName=SlipknotCuster";
    private static final String DATABASE_NAME = "User_Database_CompuMart";
    private static MongoClient mongoClient = null;

    // Create a trust manager that does not validate certificate chains
    private static TrustManager[] getTrustAllCertsManager() {
        return new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };
    }

    // Create SSL context that trusts all certificates (for development only)
    private static SSLContext createTrustAllSSLContext() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, getTrustAllCertsManager(), new java.security.SecureRandom());
            return sslContext;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            System.err.println("❌ Failed to create SSL context: " + e.getMessage());
            return null;
        }
    }

    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            long startTime = System.currentTimeMillis();

            try {
                ConnectionString connString = new ConnectionString(CONNECTION_STRING);

                MongoClientSettings settings = MongoClientSettings.builder()
                        .applyConnectionString(connString)
                        .applyToSslSettings(builder -> {
                            builder.enabled(true);
                            builder.invalidHostNameAllowed(true); // Bypass hostname verification
                            SSLContext sslContext = createTrustAllSSLContext();
                            if (sslContext != null) {
                                builder.context(sslContext);
                            }
                        })
                        .applyToConnectionPoolSettings(builder ->
                                builder.maxSize(10)
                                        .minSize(2)
                                        .maxWaitTime(5, TimeUnit.SECONDS)
                                        .maxConnectionIdleTime(10, TimeUnit.MINUTES))
                        .applyToSocketSettings(builder ->
                                builder.connectTimeout(10, TimeUnit.SECONDS)
                                        .readTimeout(30, TimeUnit.SECONDS))
                        .applyToClusterSettings(builder ->
                                builder.serverSelectionTimeout(10, TimeUnit.SECONDS))
                        .build();

                mongoClient = MongoClients.create(settings);

                // Test the connection immediately
                MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
                database.listCollectionNames().first(); // Simple operation to test connection

                long connectionTime = System.currentTimeMillis() - startTime;
                System.out.println("✅ MongoDB connection established in: " + connectionTime + "ms");

            } catch (Exception e) {
                System.err.println("❌ Failed to establish MongoDB connection: " + e.getMessage());
                e.printStackTrace();
                // Try fallback connection without SSL
                mongoClient = createFallbackConnection();
            }
        }
        return mongoClient.getDatabase(DATABASE_NAME);
    }

    // Fallback connection without SSL for development
    private static MongoClient createFallbackConnection() {
        System.out.println("🔄 Attempting fallback connection without SSL...");

        try {
            // Create connection string without SSL requirements
            String fallbackConnectionString = "mongodb+srv://eitaganas_db_user:syWgrwfcXryC0DxO@slipknotcuster.a2xm1cy.mongodb.net/?retryWrites=true&w=majority&appName=SlipknotCuster&ssl=false";

            ConnectionString connString = new ConnectionString(fallbackConnectionString);

            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connString)
                    .applyToSslSettings(builder -> {
                        builder.enabled(false); // Disable SSL entirely
                    })
                    .applyToConnectionPoolSettings(builder ->
                            builder.maxSize(5)
                                    .minSize(1)
                                    .maxWaitTime(10, TimeUnit.SECONDS))
                    .applyToSocketSettings(builder ->
                            builder.connectTimeout(15, TimeUnit.SECONDS)
                                    .readTimeout(30, TimeUnit.SECONDS))
                    .applyToClusterSettings(builder ->
                            builder.serverSelectionTimeout(15, TimeUnit.SECONDS))
                    .build();

            MongoClient client = MongoClients.create(settings);

            // Test the fallback connection
            MongoDatabase database = client.getDatabase(DATABASE_NAME);
            database.listCollectionNames().first();

            System.out.println("✅ Fallback MongoDB connection established (SSL disabled)");
            return client;

        } catch (Exception e) {
            System.err.println("❌ Fallback connection also failed: " + e.getMessage());
            throw new RuntimeException("Cannot connect to MongoDB: " + e.getMessage());
        }
    }

    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            System.out.println("🔌 MongoDB connection closed");
        }
    }

    // Method to test connection health
    public static boolean testConnection() {
        try {
            MongoDatabase database = getDatabase();
            database.listCollectionNames().first();
            System.out.println("✅ MongoDB connection test: SUCCESS");
            return true;
        } catch (Exception e) {
            System.err.println("❌ MongoDB connection test: FAILED - " + e.getMessage());
            return false;
        }
    }
}