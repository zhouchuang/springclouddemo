package user.zc.customerhystrix.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.util.JSONPObject;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import user.zc.api.entities.Dept;
import user.zc.api.service.DeptClientService;
import user.zc.api.util.SnowFlake;
import user.zc.apitcc.entities.Logs;
import user.zc.apitcc.service.LogsClientService;
import user.zc.customerhystrix.service.DeptService;

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


    @GetMapping("/consumer/dept/update/{id}/{name}")
    public Integer update(@PathVariable Long id ,@PathVariable String name){
       /* Dept dept = new Dept(id,name);
        Integer flag = deptClientService.update(dept);
        String str = JSON.toJSONString(dept);
        Boolean flag2 = logsClientService.insert(new Logs(SnowFlake.getId(),"Update",str,new Date(),dept.getClass().getSimpleName() ));
        return flag;*/
       return deptService.update(id,name);
    }


    private List<String> listFail(){
        System.out.println("controller服务降级了");
        return null;
    }
}