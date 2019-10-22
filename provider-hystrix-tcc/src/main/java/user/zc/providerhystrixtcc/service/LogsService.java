package user.zc.providerhystrixtcc.service;

import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user.zc.apitcc.entities.Logs;
import user.zc.providerhystrixtcc.dao.LogsDao;

import java.util.List;

@Service
public class LogsService {
    @Autowired
    private LogsDao deptDao;

    public List<Logs> list()throws Exception{
        return deptDao.logslist();
    }


    @LcnTransaction(propagation = DTXPropagation.SUPPORTS) //分布式事务注解
    @Transactional(rollbackFor=Exception.class)
    public Boolean insert(Logs logs){
        boolean flag =  deptDao.insert(logs);
        int i = 1/0;
        return flag;
    }
}
