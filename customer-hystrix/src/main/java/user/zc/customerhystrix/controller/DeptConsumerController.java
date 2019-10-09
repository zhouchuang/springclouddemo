package user.zc.customerhystrix.controller;

//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import user.zc.api.service.DeptClientService;

import java.util.List;

/**
 * @author GongXings
 * @createTime 30 15:48
 * @description
 */
@RestController
public class DeptConsumerController {

    @Autowired
    private DeptClientService deptClientService;
//    @HystrixCommand(fallbackMethod = "listFail")

    @GetMapping("/consumer/dept/list")
    public List list(){
        return  deptClientService.list();
    }

    private List<String> listFail(){
        System.out.println("controller服务降级了");
        return null;
    }
}