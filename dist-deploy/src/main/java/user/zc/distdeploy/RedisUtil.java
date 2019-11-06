package user.zc.distdeploy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Transaction;

import javax.annotation.PostConstruct;

@Component
public class RedisUtil {

    private final static Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    private static RedisUtil redisUtil ;

    @Autowired
    private RedisService redisService;


    @PostConstruct
    public void init() {
        redisUtil = this;
        redisUtil.redisService = this.redisService;
        System.out.println("初始化redis工具");
    }

    public static  RedisService getRedisService(){
        return  redisUtil.redisService;
    }
    public  static Object get(String key){
        Object obj  = redisUtil.redisService.get(key);
        if(obj!=null) {
            return obj;
        } else {
            return  null;
        }
    }
    public  static void set(String key,Object value,Long time){
        redisUtil.redisService.set(key,value,time);
    }
    public  static void set(String key,Object value){
        redisUtil.redisService.set(key,value);
    }
    public static void del(String key){
        redisUtil.redisService.del(key);
    }
    public static void unwatch() {
        getRedisTemplate().unwatch();
    }
    public static void watch(String key){
        getRedisTemplate().watch(key);
    }


    private static RedisTemplate<String, Object> getRedisTemplate(){
        return redisUtil.redisService.getRedisTemplate();
    }
    public static <T> T multi(SessionCallback<T> sessionCallback){
        return getRedisTemplate().execute(sessionCallback);
    }

    public static <T> void pushTask(T object){
        getRedisTemplate().opsForList().rightPush(object.getClass().getSimpleName(),object);
    }

    public static <T> T popTask(String key){
        return (T)getRedisTemplate().opsForList().leftPop(key);
    }

    public static void  broadcast(String channel){
        getRedisTemplate().convertAndSend(channel, true);
    }
    public static void  broadcast(String channel,String message){
        getRedisTemplate().convertAndSend(channel, message);
    }


}
