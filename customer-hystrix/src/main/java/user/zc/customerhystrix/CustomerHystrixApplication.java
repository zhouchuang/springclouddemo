package user.zc.customerhystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages ={"user.zc.api"})
@ComponentScan(basePackages = {"user.zc.api","user.zc.customerhystrix"})
//@EnableCircuitBreaker

public class CustomerHystrixApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerHystrixApplication.class, args);
    }

}
