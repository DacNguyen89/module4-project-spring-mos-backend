package com.codegym.mos.module4projectmos;

import com.codegym.mos.module4projectmos.config.security.DataSeedingListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.codegym.mos.module4projectmos")
@ComponentScan(basePackages = "com.codegym")
@EntityScan("com.codegym.mos.module4projectmos.model")
@EnableJpaRepositories(basePackages = {"com.codegym.mos.module4projectmos.repository"})
public class Module4ProjectMosApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder) {
        return applicationBuilder.sources(Module4ProjectMosApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Module4ProjectMosApplication.class, args);
    }

    @Autowired
    DataSeedingListener dataSeedingListener;

    @EventListener(ContextRefreshedEvent.class)
    public void dataSeeding() {
        dataSeedingListener.onApplicationEvent();
    }

    /*@EventListener(ContextRefreshedEvent.class)
    public void dataSeeding() {
        dataSeedingListener.onApplicationEvent();
    }*/
}
