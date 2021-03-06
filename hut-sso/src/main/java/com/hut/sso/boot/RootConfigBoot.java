package com.hut.sso.boot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.File;

/**
 * Created by Jared on 2016/12/11.
 */
@EnableTransactionManagement
@Configuration
@ComponentScan({"com.hut.sso.service"})
public class RootConfigBoot {


    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {

        PropertySourcesPlaceholderConfigurer config  = new PropertySourcesPlaceholderConfigurer();

        //添加一段生产环境的，配置文件代码，如果在指定位置找到配置文件则用外置配置文件，如果没有则用内置。
        File product = new File("/usr/local/servers/system-config.properties");

        if(product.exists()){
            config.setLocation(new FileSystemResource(product));
        }
        else{
            String filePath = "/META-INF/system-config.properties";
            config.setLocation(new ClassPathResource(filePath,RootConfigBoot.class.getClassLoader()));
        }

        return config;
    }

    /**
     * 数据库配置
     */
    @Bean
    public DataSource dataSource(
            @Value("${db.url}")String url,
            @Value("${db.user}")String username,
            @Value("${db.password}")String password,
            @Value("${db.driver}")String driverClassName
    ){
        DriverManagerDataSource dataSource = new DriverManagerDataSource(url, username, password);
        dataSource.setDriverClassName(driverClassName);
        return dataSource;
    }

    //用来在 MyBatis 环境中控制数据库事物的，使用即在你的service 方法上加 @Transactional 即可
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(DataSource ds){
        DataSourceTransactionManager  tm  = new DataSourceTransactionManager();
        tm.setDataSource(ds);
        return tm;
    }

}
