package user.zc.providerhystrix.subscribe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import user.zc.api.entities.Ticket;
import user.zc.api.service.DeptClientService;
import user.zc.distdeploy.RedisUtil;
import user.zc.providerhystrix.service.DeptService;

import javax.annotation.PostConstruct;


@Component
public class TicketSubscribe implements MessageListener {

    @Autowired
    private DeptService deptService;

    private static DeptService staticDeptService;

    @PostConstruct
    public void init() {
        staticDeptService = this.deptService;
    }


    @Override
    public void onMessage(Message message, byte[] bytes) {
        Ticket ticket = RedisUtil.popTask("Ticket");
        while(ticket!=null){
            staticDeptService.ticketSave(ticket);
            ticket = RedisUtil.popTask("Ticket");
        }
    }
}
