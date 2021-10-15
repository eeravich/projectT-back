package app.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.NoArgsConstructor;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

@NoArgsConstructor
public class DataSourceConf {
    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    public DataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbUrl);
        config.setAutoCommit(true);
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(10);
        return new HikariDataSource(config);
    }

    public DefaultConfiguration configuration() {
        DefaultConfiguration defaultConfiguration = new DefaultConfiguration();
        defaultConfiguration.set(dataSourceConnectionProvider());
        return defaultConfiguration;
    }

    public DataSourceConnectionProvider dataSourceConnectionProvider() {
        DataSource dataSource = createDataSource();
        return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
    }

    @Bean
    public DefaultDSLContext dslContext() {
        return new DefaultDSLContext(configuration());
    }
}
