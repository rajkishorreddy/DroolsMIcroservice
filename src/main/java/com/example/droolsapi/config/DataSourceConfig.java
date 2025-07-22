package com.example.droolsapi.config;

import io.github.cdimascio.dotenv.Dotenv;
import net.snowflake.client.jdbc.SnowflakeBasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource snowflakeDataSource() throws Exception {
        // Load env variables from .env
        Dotenv dotenv = Dotenv.load();

        String account = dotenv.get("SNOWFLAKE_ACCOUNT");
        String username = dotenv.get("SNOWFLAKE_USERNAME");
        String role = dotenv.get("SNOWFLAKE_ROLE");
        String warehouse = dotenv.get("SNOWFLAKE_WAREHOUSE");
        String database = dotenv.get("SNOWFLAKE_DATABASE");
        String schema = dotenv.get("SNOWFLAKE_SCHEMA");
        String privateKeyPath = dotenv.get("SNOWFLAKE_PRIVATE_KEY_PATH");
        String passphrase = dotenv.get("SNOWFLAKE_PRIVATE_KEY_PASSPHRASE");

        // Read and decode RSA private key
        PrivateKey privateKey = loadPrivateKey(privateKeyPath, passphrase);

        SnowflakeBasicDataSource dataSource = new SnowflakeBasicDataSource();
        dataSource.setUrl("jdbc:snowflake://" + account + ".snowflakecomputing.com");
        dataSource.setUser(username);
        dataSource.setRole(role);
        dataSource.setWarehouse(warehouse);
        dataSource.setDatabaseName(database);
        dataSource.setSchema(schema);
        dataSource.setAuthenticator("SNOWFLAKE_JWT");
        dataSource.setPrivateKey(privateKey);

        return dataSource;
    }

    private PrivateKey loadPrivateKey(String path, String passphrase) throws Exception {
        String key = new String(Files.readAllBytes(new File(path).toPath()))
                .replaceAll("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] encoded = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    }
}
