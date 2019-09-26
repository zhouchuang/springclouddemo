package user.zc.customer01.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class DemoController {
    private static final String REST_URL_PREFIX="http://PROVIDER01";
    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/consumer/dept/discovery")
    public Object discovery(){
        return  restTemplate.getForObject(
                REST_URL_PREFIX+"/dept/discovery",
                Object.class);
    }


    @RequestMapping(value = "/consumer/dept/list")
    public Object list(){
        return  restTemplate.getForObject(
                REST_URL_PREFIX+"/dept/list",
                Object.class);
    }
}
