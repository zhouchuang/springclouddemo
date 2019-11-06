package user.zc.apitcc.service;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import user.zc.apitcc.entities.Logs;

import java.util.List;

/**
 * @author zhouchuang
 * @createTime 31 11:54
 * @description
 */
@FeignClient(value = "PROVIDERTCC",fallbackFactory = LogsClientServiceFallBackFactory.class)
public interface LogsClientService {
    @GetMapping("/logs/list")
    public  List<Logs> list();

    @PostMapping("/logs/insert")
    public  Boolean insert(Logs logs);

    @PostMapping("/logs/tcctest")
    public  Boolean tcctest(Logs logs);
}