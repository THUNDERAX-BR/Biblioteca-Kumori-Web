package com.bento.BibliotecaKumori.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/kumoridb");
        ds.setUsername("kumori");
        ds.setPassword("a7X@pL#9zWq1!Km$Tf8&nB2^RsE0*VhY");
        return ds;
    }
}