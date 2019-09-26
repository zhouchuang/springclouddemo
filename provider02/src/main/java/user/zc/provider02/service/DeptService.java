package user.zc.provider02.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import user.zc.provider02.dao.DeptDao;

import java.util.List;

@Service
public class DeptService {
    @Autowired
    private DeptDao deptDao;

    public List<String> list()throws Exception{
        return deptDao.deptlist();
    }
}
