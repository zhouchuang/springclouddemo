package user.zc.apitcc.service;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import user.zc.apitcc.entities.Logs;

import java.util.List;

/**
 * @author Zhouchuang
 * @createTime 31 14:52
 * @description 统一处理DeptClientService这个类中的熔断
 */
@Component
public class LogsClientServiceFallBackFactory implements FallbackFactory<LogsClientService> {
    @Override
    public LogsClientService create(Throwable throwable) {
        return new LogsClientService() {
            @Override
            public List list(){
                System.out.println("list触发降级了："+throwable.getMessage());
                return null;
            }

            @Override
            public List<String> deptlist() {
                System.out.println("deptlist触发降级了："+throwable.getMessage());
                return null;
            }

            @Override
            public Boolean insert(Logs logs) {
                System.out.println("insert触发降级了："+throwable.getMessage());
                return Boolean.FALSE;
            }
        };
    }
}