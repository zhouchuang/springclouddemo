package user.zc.redisclusterdemo;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class RedisClusterConfig {
    @Autowired
    private RedisProperty redisProperty;

    @Bean
    public JedisCluster getJedisPoolConfig(){

        String[] cNodes = redisProperty.getClusterNodes().split(",");
        Set<HostAndPort> nodes =new HashSet<>();
        //分割出集群节点
        for(String node : cNodes) {
            String[] hp = node.split(":");
            nodes.add(new HostAndPort(hp[0].trim(),Integer.parseInt(hp[1].trim())));
        }
        JedisPoolConfig jedisPoolConfig =new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(redisProperty.getMaxIdle());
        jedisPoolConfig.setMaxWaitMillis(redisProperty.getMaxWaitMillis());
        //创建集群对象
//        return new JedisCluster(nodes,redisProperty.getCommandTimeout(),jedisPoolConfig);
        return new JedisCluster(nodes,redisProperty.getTimeout(),redisProperty.getCommandTimeout(),redisProperty.getMaxIdle(),redisProperty.getPassword(),jedisPoolConfig);
    }
}
