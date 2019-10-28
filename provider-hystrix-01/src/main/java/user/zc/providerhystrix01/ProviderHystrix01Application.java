package user.zc.providerhystrix01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import user.zc.providerhystrix01.subscribe.TicketSubscribe;
import user.zc.providerhystrix01.subscribe.TicketSubscribeFactory;

@SpringBootApplication
@EnableEurekaClient //本服务启动后会自动注册进eureka服务中
@EnableDiscoveryClient //服务发现
@EnableCircuitBreaker//对hystrixR熔断机制的支持
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan(basePackages = {"user.zc.providerhystrix01","user.zc.distdeploy"})
//@EnableDistributedTransaction
public class ProviderHystrix01Application {




    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();

        container.setConnectionFactory(redisConnectionFactory);

        /**
         * 添加订阅者监听类，数量不限.PatternTopic定义监听主题,这里监听BroadcastTask主题
         */
        container.addMessageListener(TicketSubscribeFactory.create(), new PatternTopic(TicketSubscribe.class.getSimpleName()));
        return container;

    }



    public static void main(String[] args) {
        SpringApplication.run(ProviderHystrix01Application.class, args);
    }

}
