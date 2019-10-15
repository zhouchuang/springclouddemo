package user.zc.providerhystrix.api;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import user.zc.api.entities.Dept;
import user.zc.providerhystrix.service.DeptService;

import java.util.Arrays;
import java.util.List;

@RestController
public class DemoController {

    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private DeptService deptService;


    @RequestMapping(value = "/dept/discovery",method = RequestMethod.GET)
    public Object discovery(){
        List<String> list = discoveryClient.getServices();
        List<ServiceInstance> instances = discoveryClient.getInstances("provider".toUpperCase());
        for (ServiceInstance element :instances){
            System.out.println(element.getServiceId());
            System.out.println(element.getHost());
            System.out.println(element.getPort());
            System.out.println(element.getUri());
        }
        return this.discoveryClient;
    }

    @RequestMapping(value = "/dept/list",method = RequestMethod.GET)
    @HystrixCommand(fallbackMethod = "listFail")
    public Object list(){
        List<String> list = null;
        try {
            list = deptService.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @RequestMapping(value = "/logs/list",method = RequestMethod.GET)
    public Object logslist(){
        return Arrays.asList("我是DeptFeign".split(","));
    }


    @RequestMapping(value = "/dept/update",method = RequestMethod.POST)
    public Object update(@RequestBody Dept dept){
        try {
            return deptService.update(dept);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Object listFail(){
        return Arrays.asList("哈哈哈,我故意熔断了".split(","));
    }


}
