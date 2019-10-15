package user.zc.api.service;


import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import user.zc.api.entities.Dept;

import java.util.List;

/**
 * @author zhouchuang
 * @createTime 31 11:54
 * @description
 */
@FeignClient(value = "PROVIDER",fallbackFactory = DeptClientServiceFallBackFactory.class)
public interface DeptClientService {
    @GetMapping("/dept/list")
    public  List<String> list();

    @GetMapping("/logs/list")
    public  List<Object> logslist();

    @PostMapping("/dept/update")
    public Integer update(Dept dept);
}