package user.zc.providerhystrixtcc.api;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import user.zc.apitcc.entities.Logs;
import user.zc.providerhystrixtcc.service.LogsService;

import java.util.Arrays;
import java.util.List;

@RestController
public class DemoController {

    @Autowired
    private LogsService logsService;
    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping(value = "/logs/discovery",method = RequestMethod.GET)
    public Object discovery(){
        List<String> list = discoveryClient.getServices();
        List<ServiceInstance> instances = discoveryClient.getInstances("providertcc".toUpperCase());
        for (ServiceInstance element :instances){
            System.out.println(element.getServiceId());
            System.out.println(element.getHost());
            System.out.println(element.getPort());
            System.out.println(element.getUri());
        }
        return this.discoveryClient;
    }


    @RequestMapping(value = "/logs/list",method = RequestMethod.GET)
    @HystrixCommand(fallbackMethod = "listFail")
    public Object list(){
        List<Logs> list = null;
        try {
            list = logsService.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @RequestMapping(value = "/logs/insert",method = RequestMethod.POST)
    public Boolean insert(@RequestBody Logs logs){
        return  logsService.insert(logs);
    }

    public Object listFail(){
        return null;
    }


}
