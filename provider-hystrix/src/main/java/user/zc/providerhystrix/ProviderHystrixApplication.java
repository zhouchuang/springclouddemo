package user.zc.providerhystrix;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import user.zc.api.entities.Ticket;
import user.zc.providerhystrix.service.DeptService;
import user.zc.providerhystrix.subscribe.TicketSubscribe;

@SpringBootApplication
@EnableEurekaClient //本服务启动后会自动注册进eureka服务中
@EnableDiscoveryClient //服务发现
@EnableCircuitBreaker//对hystrixR熔断机制的支持
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan(basePackages = {"user.zc.providerhystrix","user.zc.distdeploy"})
//@EnableDistributedTransaction
public class ProviderHystrixApplication {




    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();

        container.setConnectionFactory(redisConnectionFactory);

        /**
         * 添加订阅者监听类，数量不限.PatternTopic定义监听主题,这里监听BroadcastTask主题
         */
        container.addMessageListener(new TicketSubscribe(), new PatternTopic(TicketSubscribe.class.getSimpleName()));
        return container;

    }



    public static void main(String[] args) {
        SpringApplication.run(ProviderHystrixApplication.class, args);
    }

}
