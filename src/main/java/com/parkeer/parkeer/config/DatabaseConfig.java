package com.parkeer.parkeer.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.lang.NonNull;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;


@Slf4j
@Configuration
@EnableR2dbcRepositories
@EnableConfigurationProperties({R2dbcProperties.class, FlywayProperties.class})
class DatabaseConfig extends AbstractR2dbcConfiguration {

    private static final String MIGRATE = "migrate";
    private static final String SCHEMA_SQL = "schema.sql";

    @Value("${spring.r2dbc.url}")
    private String r2dbcURL;

    @Bean(initMethod = MIGRATE)
    public Flyway flyway(FlywayProperties flywayProperties, R2dbcProperties r2dbcProperties) {
        return Flyway.configure()
                .dataSource(
                        flywayProperties.getUrl(),
                        r2dbcProperties.getUsername(),
                        r2dbcProperties.getPassword()
                )
                .locations(flywayProperties.getLocations().toArray(String[]::new))
                .baselineOnMigrate(true)
                .load();
    }

    @Override
    @NonNull
    public ConnectionFactory connectionFactory() {
        return ConnectionFactories.get(r2dbcURL);
    }

    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource(SCHEMA_SQL)));
        initializer.setDatabasePopulator(populator);
        return initializer;
    }

}
