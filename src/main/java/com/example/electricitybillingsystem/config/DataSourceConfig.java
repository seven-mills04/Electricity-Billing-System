package com.example.electricitybillingsystem.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.jdbc.DataSourceBuilder;
import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DataSourceConfig {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    @Bean
    @Primary
    public DataSource dataSource() {
        String dbUrl = System.getenv("DATABASE_URL");
        String username = System.getenv("DATABASE_USERNAME");
        String password = System.getenv("DATABASE_PASSWORD");

        logger.info("Initializing DataSource. DATABASE_URL environment variable is present: {}", dbUrl != null);

        if (dbUrl != null && dbUrl.startsWith("mysql://")) {
            logger.info("Detected mysql:// connection string format. Parsing and converting to JDBC...");
            try {
                URI uri = new URI(dbUrl);
                String host = uri.getHost();
                int port = uri.getPort();
                String path = uri.getPath();
                String userInfo = uri.getUserInfo();

                String jdbcUrl = "jdbc:mysql://" + host + (port != -1 ? ":" + port : "") + path;
                logger.info("Successfully converted to JDBC URL: {}", jdbcUrl);

                DataSourceBuilder<?> builder = DataSourceBuilder.create();
                builder.url(jdbcUrl);

                if (userInfo != null && userInfo.contains(":")) {
                    String[] creds = userInfo.split(":");
                    builder.username(creds[0]);
                    builder.password(creds[1]);
                    logger.info("Extracted username from connection string: {}", creds[0]);
                } else {
                    if (username != null) {
                        builder.username(username);
                    }
                    if (password != null) {
                        builder.password(password);
                    }
                }

                builder.driverClassName("com.mysql.cj.jdbc.Driver");
                return builder.build();
            } catch (URISyntaxException e) {
                logger.error("Failed to parse mysql:// DATABASE_URL connection string: {}", e.getMessage());
            }
        }

        // Fallback to standard property configuration, reading from DATABASE_URL as a direct JDBC url,
        // or falling back to local database defaults if environment is empty.
        logger.info("Using standard JDBC datasource settings...");
        String activeUrl = dbUrl != null ? dbUrl : "jdbc:mysql://localhost:3306/electricitybillingsystem?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true";
        String activeUser = username != null ? username : "root";
        String activePass = password != null ? password : "root";

        return DataSourceBuilder.create()
                .url(activeUrl)
                .username(activeUser)
                .password(activePass)
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }
}
