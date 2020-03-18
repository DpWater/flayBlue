package com.zzxj.flyBlue.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * Created by Administrator on 2019/12/13.
 */
@Configuration
@MapperScan(basePackages = "com.demo.fin.orm.manage.mapper", sqlSessionTemplateRef  = "mexcSqlSessionTemplate")
public class DataSource1Config {
    @Value("classpath*:mapper/*.xml")
    private Resource[] mapperLocations;

    @Bean(name = "baiduDataSourece")
    @ConfigurationProperties(prefix = "spring.datasource.manage")
    @Primary
    public DataSource mexcDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "baiDuSqlSessionFactory")
    @Primary /*此处必须在主数据库的数据源配置上加上@Primary*/
    public SqlSessionFactory mSqlSessionFactory(@Qualifier("baiduDataSourece") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
      /*加载mybatis全局配置文件*/
//        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResource("classpath:mapper/*.xml"));
      /*加载所有的mapper.xml映射文件*/
        bean.setMapperLocations(mapperLocations);
        return bean.getObject();
    }

    @Bean(name = "mTransactionManager")
    @Primary
    public DataSourceTransactionManager mTransactionManager(@Qualifier("baiduDataSourece") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "mSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate mSqlSessionTemplate(@Qualifier("baiDuSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
