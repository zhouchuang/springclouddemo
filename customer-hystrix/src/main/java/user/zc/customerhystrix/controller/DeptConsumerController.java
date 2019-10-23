package user.zc.customerhystrix.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import user.zc.api.service.DeptClientService;
import user.zc.apitcc.service.LogsClientService;
import user.zc.customerhystrix.Tools;
import user.zc.customerhystrix.service.DeptService;
import user.zc.distdeploy.RedisUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author Zhouchuang
 * @createTime 30 15:48
 * @description
 */
@RestController
public class DeptConsumerController {

    @Autowired
    private DeptService deptService;

    @Autowired
    private DeptClientService deptClientService;

    @Autowired
    private LogsClientService logsClientService;
//    @HystrixCommand(fallbackMethod = "listFail")

    @GetMapping("/consumer/dept/list")
    public List list(){
        return  deptClientService.list();
    }

    @GetMapping("/consumer/logs/list")
    public List logs(){
        return  logsClientService.list();
    }


    @GetMapping("/consumer/dept/update/{id}/{name}/{flag}")
    public Integer update(@PathVariable Long id ,@PathVariable String name,@PathVariable Boolean flag){
        try {
            return deptService.update(id,name,flag);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @GetMapping("/consumer/redis/set/{key}/{value}")
    public String set(@PathVariable String key,@PathVariable String value){
        RedisUtil.set(key,value);
        return RedisUtil.get(key);
    }
    @GetMapping("/consumer/redis/get/{key}")
    public String set(@PathVariable String key){
        return RedisUtil.get(key);
    }

    @GetMapping("/consumer/buy")
    public String buy(HttpServletRequest request){
        String key = "goods";

        String msg =  RedisUtil.multi(new SessionCallback<String>() {
           @Override
           public String execute(RedisOperations operations) throws DataAccessException {
               List<Object> result = null;
               int num = 0;
               do {
                   int count = 0;
                   operations.watch(key);  // watch某个key,当该key被其它客户端改变时,则会中断当前的操作
                   String value = (String) operations.opsForValue().get(key);
                   if (!StringUtils.isEmpty(value)) {
                       count = Integer.parseInt(value);
                   }
                   if(count>0){
                       count = count - 1;
                       operations.multi(); //开始事务
                       operations.delete(key);
                       operations.opsForValue().set(key, String.valueOf(count));
                       try {
                           result = operations.exec(); //提交事务
                           operations.unwatch();  //提交事务后，key再被改变不用管了，反正我提交了
                           if(result.size()>0){
                               num = count+1;
                               deptService.ticketSave(num,Tools.getIpAddress(request));
                           }
                       } catch (Exception e) { //如果key被改变,提交事务时这里会报异常
                           e.printStackTrace();
                           return "竞争失败";
                       }
                   }else {
                       operations.unwatch();
                       return "没有票了啊";
                   }
               } while (result == null||result.size()==0); //如果失败则重试
               return "抢到了第"+num+"张票";
           }
        });
        return msg;
    }


    private List<String> listFail(){
        System.out.println("controller服务降级了");
        return null;
    }
}