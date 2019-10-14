package user.zc.providerhystrixtcc.api;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import user.zc.apitcc.entities.Logs;
import user.zc.providerhystrixtcc.service.LogsService;
import java.util.List;

@RestController
public class DemoController {

    @Autowired
    private LogsService logsService;

    @RequestMapping(value = "/logs/list",method = RequestMethod.GET)
    @HystrixCommand(fallbackMethod = "listFail")
    public Object list(){
        List<Logs> list = null;
        try {
            list = logsService.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    @RequestMapping(value = "/logs/insert",method = RequestMethod.POST)
    public Integer insert(@RequestBody Logs logs){
        try {
            return  logsService.insert(logs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Object listFail(){
        return null;
    }


}
