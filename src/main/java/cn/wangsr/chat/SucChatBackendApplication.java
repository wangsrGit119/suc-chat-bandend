package cn.wangsr.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Administrator
 */
@EnableJpaRepositories(basePackages = {"cn.wangsr.chat.dao"})
@EntityScan(basePackages = {"cn.wangsr.chat.model"})
@SpringBootApplication
public class SucChatBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SucChatBackendApplication.class, args);
    }

}
