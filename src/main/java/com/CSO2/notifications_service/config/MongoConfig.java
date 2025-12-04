package com.CSO2.notifications_service.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB Configuration
 * Configures MongoDB connection using URI and database name from application properties
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.CSO2.notifications_service.repository")
public class MongoConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    /**
     * Creates MongoClient bean using the configured MongoDB URI
     * @return MongoClient instance
     */
    @Bean
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(mongoUri);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        
        return MongoClients.create(mongoClientSettings);
    }

    /**
     * Creates MongoDatabaseFactory bean for database access
     * @return MongoDatabaseFactory instance
     */
    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory() {
        return new SimpleMongoClientDatabaseFactory(mongoClient(), getDatabaseName());
    }

    /**
     * Creates MongoTemplate bean for MongoDB operations
     * @return MongoTemplate instance
     */
    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDatabaseFactory());
    }

    /**
     * Extracts database name from MongoDB URI
     * @return database name
     */
    private String getDatabaseName() {
        ConnectionString connectionString = new ConnectionString(mongoUri);
        String database = connectionString.getDatabase();
        
        if (database == null || database.isEmpty()) {
            throw new IllegalStateException("Database name must be specified in MongoDB URI");
        }
        
        return database;
    }
}
