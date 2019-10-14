package user.zc.providerhystrixtcc.service;

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

    @Transactional
    public Integer insert(Logs logs)throws Exception{
        return deptDao.insert(logs);
    }
}
