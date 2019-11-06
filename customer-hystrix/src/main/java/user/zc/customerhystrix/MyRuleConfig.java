package user.zc.customerhystrix;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RetryRule;
import com.netflix.loadbalancer.RoundRobinRule;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

//@Configuration
public class MyRuleConfig {
    @Bean
    public IRule txxRule() {
        return new RoundRobinRule();
    }

    @Bean
    @LoadBalanced //ribbon实现的一套 ==客户端、负载均衡的工具
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
