package user.zc.customerfeign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
/**
 * 在启动该微服务式是能去加载我们定义的Feign配置类
 */
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "user.zc.api")
@ComponentScan({"user.zc.api","user.zc.customerfeign"})
public class CustomerFeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerFeignApplication.class, args);
    }

}
