package user.zc.providerhystrix01.subscribe;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import user.zc.api.entities.Ticket;
import user.zc.distdeploy.RedisUtil;
import user.zc.providerhystrix01.service.DeptService;

public class TicketSubscribe implements MessageListener {

    private  DeptService deptService;


    public TicketSubscribe(DeptService deptService) {
        this.deptService = deptService;
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        Ticket ticket = RedisUtil.popTask("Ticket");
        while(ticket!=null){
            deptService.ticketSave(ticket);
            ticket = RedisUtil.popTask("Ticket");
        }
    }
}
