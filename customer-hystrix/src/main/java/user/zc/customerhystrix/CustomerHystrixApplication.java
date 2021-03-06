package user.zc.customerhystrix;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages ={"user.zc.api","user.zc.apitcc"})
@ComponentScan(basePackages = {"user.zc.api","user.zc.apitcc","user.zc.customerhystrix","user.zc.distdeploy"})
//@RibbonClient(name="PROVIDERTCC",configuration = MyRuleConfig.class)
//@EnableDistributedTransaction
public class CustomerHystrixApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerHystrixApplication.class, args);
    }

}
