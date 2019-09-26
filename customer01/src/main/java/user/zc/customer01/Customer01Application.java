package user.zc.customer01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient

public class Customer01Application {

    public static void main(String[] args) {
        SpringApplication.run(Customer01Application.class, args);
    }

}
