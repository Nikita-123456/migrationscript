package com.migration.example.migrationscript.mssql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class mssql {

    @Value("${spring.datasource.url}")
    private String connectionString;
    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DataSource readOnlyDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(connectionString);
        config.setUsername(username);
        config.setPassword(password);
        config.setReadOnly(true);
        config.setAutoCommit(true);
        return new HikariDataSource(config);
    }
}
