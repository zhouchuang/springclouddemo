package user.zc.redisclusterdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@RestController
public class RedisClusterDemoApplication {

    //分布式redis
    @Autowired
    private RedisClusterConfig redisClusterConfig;

    public static void main(String[] args) {
        SpringApplication.run(RedisClusterDemoApplication.class, args);
    }
    @GetMapping("/test")
    public void test(){
        redisClusterConfig.getJedisPoolConfig().set("name","lili");
        System.out.println(redisClusterConfig.getJedisPoolConfig().get("name"));
    }
}
