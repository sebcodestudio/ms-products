//package com.sebcode.msproducts.security.config;
//
//import org.flywaydb.core.Flyway;
//import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class FlywayConfig {
//
//    @Bean
//    public FlywayMigrationStrategy cleanMigrateStrategy() {
//        return flyway -> {
//            // Esto asegura que Flyway tome el control antes que Hibernate
//            flyway.migrate();
//        };
//    }
//
//    @Bean(initMethod = "migrate")
//    public Flyway flyway(DataSource dataSource) {
//        return Flyway.configure()
//                .dataSource(dataSource)
//                .baselineOnMigrate(true)
//                .locations("classpath:db/migration")
//                .load();
//    }
//}