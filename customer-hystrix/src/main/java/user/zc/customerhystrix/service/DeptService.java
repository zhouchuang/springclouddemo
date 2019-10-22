package user.zc.customerhystrix.service;

import com.alibaba.fastjson.JSON;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user.zc.api.entities.Dept;
import user.zc.api.service.DeptClientService;
import user.zc.api.util.SnowFlake;
import user.zc.apitcc.entities.Logs;
import user.zc.apitcc.service.LogsClientService;

import java.util.Date;

@Service
public class DeptService {

    @Autowired
    private DeptClientService deptClientService;

    @Autowired
    private LogsClientService logsClientService;


    @LcnTransaction
    @Transactional
    public Integer update(Long id , String name) {
        Dept dept = new Dept(id,name);
        Integer flag = deptClientService.update(dept);
        String str = JSON.toJSONString(dept);
        Boolean flag2 = logsClientService.insert(new Logs(SnowFlake.getId(),"Update",str,new Date(),dept.getClass().getSimpleName() ));
        if(flag2){
            throw  new IllegalStateException("插入异常");
        }else{
            return flag;
        }

    }
}
