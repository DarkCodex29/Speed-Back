package com.hochschild.speed.back.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "com.hochschild.speed.back.repository.speed", entityManagerFactoryRef = "speedEntityManagerFactory", transactionManagerRef = "speedTransactionManager")
public class SpeedDataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties("jdbc.datasource.speed")
    public DataSourceProperties speedDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("jdbc.datasource.speed.configuration")
    public DataSource speedDataSource() {
        return speedDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Primary
    @Bean(name = "speedEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean speedEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder.dataSource(speedDataSource())
                .packages("com.hochschild.speed.back.model.domain.speed").build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager speedTransactionManager(
            final @Qualifier("speedEntityManagerFactory") LocalContainerEntityManagerFactoryBean speedEntityManagerFactory) {
        return new JpaTransactionManager(speedEntityManagerFactory.getObject());
    }
}
