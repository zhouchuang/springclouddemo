package user.zc.providerhystrixtcc.service;

import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.codingapi.txlcn.tc.annotation.TccTransaction;
import com.codingapi.txlcn.tc.annotation.TxcTransaction;
import com.codingapi.txlcn.tracing.TracingContext;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user.zc.apitcc.entities.Logs;
import user.zc.providerhystrixtcc.dao.LogsDao;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class LogsService {

    private ConcurrentHashMap<String, Long> ids = new ConcurrentHashMap<>();

    @Autowired
    private LogsDao logsDao;

    public List<Logs> list()throws Exception{
        return logsDao.logslist();
    }


    @LcnTransaction(propagation = DTXPropagation.SUPPORTS) //分布式事务注解
    @Transactional
    public Boolean insert(Logs logs){
        logsDao.insert(logs);
        return true;
    }

    @TccTransaction(propagation = DTXPropagation.SUPPORTS,confirmMethod = "confirmRpc",cancelMethod = "cancelRpc") //分布式事务注解
    @Transactional
    public Boolean tcctest(Logs logs){
        logsDao.insert(logs);
        ids.put(TracingContext.tracing().groupId(), logs.getId());
        return true;
    }

    public void confirmRpc(String value) {
        log.info("tcc-confirm-" + TracingContext.tracing().groupId());
        ids.remove(TracingContext.tracing().groupId());
    }

    public void cancelRpc(String value) {
        log.info("tcc-cancel-" + TracingContext.tracing().groupId());
        Long kid = ids.get(TracingContext.tracing().groupId());
        logsDao.delete(kid);
    }

}
