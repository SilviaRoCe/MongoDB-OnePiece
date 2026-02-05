package org.example.database;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    // cambiar TU_CONTRASEÑA
    private static final String URI = "mongodb+srv://natalolmao_db_user:TU_CONTRASEÑA@cluster0.mw8bcwz.mongodb.net/?appName=Cluster0";
    private static MongoClient mongoClient = null;

    public static MongoDatabase getDatabase(String dbName) {
        if (mongoClient == null) {
            ServerApi serverApi = ServerApi.builder()
                    .version(ServerApiVersion.V1)
                    .build();

            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(URI))
                    .serverApi(serverApi)
                    .build();

            mongoClient = MongoClients.create(settings);
        }
        return mongoClient.getDatabase(dbName);
    }
}
