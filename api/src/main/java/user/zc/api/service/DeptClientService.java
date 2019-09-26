package user.zc.api.service;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author zhouchuang
 * @createTime 31 11:54
 * @description
 */
@FeignClient(value = "PROVIDER")
public interface DeptClientService {
    @RequestMapping(value = "/dept/list",method = RequestMethod.GET)
    List<String> list();
}