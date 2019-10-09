package user.zc.api.service;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author Zhouchuang
 * @createTime 31 14:52
 * @description 统一处理DeptClientService这个类中的熔断
 */
@Component
public class DeptClientServiceFallBackFactory implements FallbackFactory<DeptClientService> {
    @Override
    public DeptClientService create(Throwable throwable) {
        return new DeptClientService () {
            @Override
            public List list(){
                System.out.println("触发降级了。。。");
                return Arrays.asList(("啥都没有吧,哈哈哈,"+throwable.getMessage()).split(","));
            }
        };
    }
}