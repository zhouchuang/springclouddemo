package user.zc.providerhystrix.subscribe;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import user.zc.providerhystrix.service.DeptService;

import javax.annotation.PostConstruct;

@Component
public class TicketSubscribeFactory {
    @Autowired
    private  DeptService deptService;
    private static  DeptService staticDeptService;
    @PostConstruct
    public void init() {
        staticDeptService = this.deptService;
    }


    private static class TicketSubscribeHolder {
        public final static TicketSubscribe instance = new TicketSubscribe(staticDeptService);
    }

    public static TicketSubscribe create(){
        return TicketSubscribeHolder.instance;
    }
}
