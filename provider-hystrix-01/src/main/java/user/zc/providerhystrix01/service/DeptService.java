package user.zc.providerhystrix01.service;

import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user.zc.api.entities.Dept;
import user.zc.api.entities.Ticket;
import user.zc.distdeploy.RedisUtil;
import user.zc.providerhystrix01.dao.DeptDao;

import java.util.List;

@Service
public class DeptService {
    @Autowired
    private DeptDao deptDao;

    public List<String> list()throws Exception{
        return deptDao.deptlist();
    }

    @LcnTransaction(propagation = DTXPropagation.SUPPORTS) //分布式事务注解
    @Transactional
    public Integer update(Dept dept){
        return deptDao.update(dept);
    }

    public Integer ticketSave(Ticket ticket){
        return deptDao.ticketSave(ticket);
    }

    public void ticketSaveTask(Ticket ticket){
        RedisUtil.pushTask(ticket);
        RedisUtil.broadcast("TicketSubscribe");
    }
}
