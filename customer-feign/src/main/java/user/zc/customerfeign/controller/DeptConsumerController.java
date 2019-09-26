package user.zc.customerfeign.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import user.zc.api.service.DeptClientService;

import java.util.List;

/**
 * @author GongXings
 * @createTime 30 15:48
 * @description
 */
@RestController
public class DeptConsumerController {

    @Autowired
    private DeptClientService deptClientService;

    @RequestMapping("/consumer/dept/list")
    public List list(){
        return  deptClientService.list();
    }
}