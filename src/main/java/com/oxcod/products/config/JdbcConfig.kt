package com.oxcod.products.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.JdbcClient
import javax.sql.DataSource

@Configuration
class JdbcConfig {
    
    @Bean
    fun jdbcTemplate(dataSource: DataSource): JdbcTemplate {
        return JdbcTemplate(dataSource)
    }
    
    @Bean
    fun jdbcClient(jdbcTemplate: JdbcTemplate): JdbcClient {
        return JdbcClient.create(jdbcTemplate)
    }
}