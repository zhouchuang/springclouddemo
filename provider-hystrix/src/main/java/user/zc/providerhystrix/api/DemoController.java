package user.zc.providerhystrix.api;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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
        List<ServiceInstance> instances = discoveryClient.getInstances("provider01".toUpperCase());
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
            list.add("provider-hystrix");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Object listFail(){
        return Arrays.asList("哈哈哈,我故意熔断了".split(","));
    }


}
