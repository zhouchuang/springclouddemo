package user.zc.customer_ribbon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

@SpringBootApplication
@EnableEurekaClient
public class CustomerRibbonApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerRibbonApplication.class, args);
    }

}
