package user.zc.providerhystrix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import user.zc.api.entities.Dept;
import user.zc.providerhystrix.dao.DeptDao;

import java.util.List;

@Service
public class DeptService {
    @Autowired
    private DeptDao deptDao;

    public List<String> list()throws Exception{
        return deptDao.deptlist();
    }

//    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Transactional
    public Integer update(Dept dept)throws Exception{
        return deptDao.update(dept);
    }
}
