package com.unicom.access.conf;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author mrChen
 * @date 2021/1/10 14:44
 */
@Configuration

@MapperScan(basePackages = "com.unicom.access.mapper", sqlSessionFactoryRef = "masterSqlSessionFactory")
public class MasterDataSourceConfig {

    @Value("${spring.datasource.master.driverClassName}")
    private String dbDriver;

    @Value("${spring.datasource.master.url}")
    private String dbUrl;

    @Value("${spring.datasource.master.username}")
    private String dbUsername;

    @Value("${spring.datasource.master.password}")
    private String dbPassword;


    @Bean(name = "masterDataSource")
    @Primary
    public DataSource dataSourceLocal() throws Exception {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(dbDriver);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    @Bean(name = "masterSqlSessionFactory")
    @Primary
    public MybatisSqlSessionFactoryBean test1SqlSessionFactory(@Qualifier("masterDataSource") DataSource datasource)
            throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(datasource);
        return bean;
    }

    @Bean("masterSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate masterSqlSessionTemplate(
            @Qualifier("masterSqlSessionFactory") SqlSessionFactory sessionfactory) {
        return new SqlSessionTemplate(sessionfactory);
    }
}
