package util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {

    private static HikariDataSource dataSource;

    static {
        try {
            HikariConfig config = new HikariConfig();
 
            config.setJdbcUrl("jdbc:postgresql://localhost:5432/news_aggregator");
            config.setUsername("postgres");
            config.setPassword("ravi");
            config.setDriverClassName("org.postgresql.Driver");

            config.setMaximumPoolSize(10);        
            config.setMinimumIdle(5);          
            config.setMaxLifetime(60000);        
            config.setIdleTimeout(30000);        
            config.setConnectionTimeout(30000);  

            dataSource = new HikariDataSource(config);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize HikariCP", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void closeDataSource() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
