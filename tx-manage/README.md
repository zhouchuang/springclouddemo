https://blog.csdn.net/pw191410147/article/details/95494813

版权声明：本文为博主原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接和本声明。
本文链接：https://blog.csdn.net/pw191410147/article/details/95494813
本文档采用框架版本，请严格按照文档版本，否则将可能造成版本冲突
框架名称	框架版本
springboot	2.0.4.RELEASE
springcloud	Finchley.SR1
jdk	1.8
tx-lcn	5.0.2.RELEASE
使用了哪些springcloud组件
组件名称	作用
eureka	注册中心
zuul	网关
hystrix	断路器
feign	声明式远程调用组件
config	配置中心
微服务架构流程图


Eureka搭建流程
pom文件

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.4.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.dsk</groupId>
    <artifactId>dsk_eureka</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>dsk_eureka</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Finchley.SR1</spring-cloud.version>
    </properties>

    <dependencies>
        <!--引入注册中心的服务端依赖-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
        <!--安全组件，目的给注册中心加密-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
52
53
54
55
56
57
58
59
60
61
62
配置文件yml：

spring:
  security:
      user:
        name: dsj #注册中心登录用户名和密码
        password: 123456
server:
  port: 8761
eureka:
  client:
    register-with-eureka: false #测试环境不需要高可用
    fetch-registry: false
    service-url:
      defaultZone: http://dsj:123456@localhost:8761/eureka
  instance:
      prefer-ip-address: true #显示ip
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
因为新版本启动认证需要改变认证方式，我们需要在启动类的目录下新建一个config包新建一个类，如下:

package com.dsk.dsk_eureka.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author 彭伟
 * @Date 2019/4/3 10:05
 * 改变认证方式
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/**").and().authorizeRequests().anyRequest().authenticated().and().httpBasic();
    }
}

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
eureka细节问题:
1、Eureka Server
    提供服务注册和发现
2、Service Provider
    服务提供方
    将自身服务注册到Eureka，从而使服务消费方能够找到
3、Service Consumer
    服务消费方
    从Eureka获取注册服务列表，从而能够消费服务
4,加入安全认证是为了提高安全性，防止服务信息泄漏。
1
2
3
4
5
6
7
8
9
最后启动类上面加上@EnableEurekaServer注解,
到此，注册中心搭建完毕.

搭建测试生产服务
pom文件如下:

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.4.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.dsk</groupId>
    <artifactId>dsk_xcx</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>dsk_xcx</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Finchley.SR1</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!--dashborad监控需要-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.0.0</version>
        </dependency>
        <!--注册中心的客户端依赖-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        <!-- alibaba的druid数据库连接池 -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.9</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
52
53
54
55
56
57
58
59
60
61
62
63
64
65
66
67
68
69
70
71
72
73
74
75
76
77
78
79
80
81
82
83
配置文件application.yml如下:

eureka:
  client:
    serviceUrl:
      defaultZone: http://dsj:123456@localhost:8761/eureka
  instance:
      instance-id: ${spring.cloud.client.ip-address}:${server.port}
      prefer-ip-address: true #显示ip
server:
  port: 8070
spring:
  application:
    name: dsk-xcx
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    druid:
          filters: stat # 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
          driver-class-name: com.mysql.jdbc.Driver
          url: jdbc:mysql://127.0.0.1:3306/testcloud?useUnicode=true&characterEncoding=utf-8
          username: root
          password: 
          #配置初始化大小/最小/最大
          initialSize: 1
          minIdle: 1
          maxActive: 20
          #获取连接等待超时时间
          maxWait: 60000
          #间隔多久进行一次检测，检测需要关闭的空闲连接
          timeBetweenEvictionRunsMillis: 60000
          #一个连接在池中最小生存的时间
          minEvictableIdleTimeMillis: 300000
#开始配置mybatis
mybatis:
  mapper-locations: classpath:mapper/*.xml #指定mapper文件地址
  type-aliases-package: com.dsk.dsk_xcx.modle #指定别名包
logging:
  level:
    com.codingapi.txlcn: debug #配置com.dsk.dsk_xcx包下面所有类以debug日志级别输出
mydurid: #配置druid的监控页面安全认证
  username: admin
  password: 123456
#暴露全部的监控信息
management:
  endpoints:
    web:
      exposure:
        include: "*"
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
因为此处开起了druid的监控系统，所以需要加上配置类,创建config包，代码如下:

package com.dsk.dsk_xcx.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 彭伟
 * 配置Druid监控服务，因为主类里面关闭了自动配置
 */
@Configuration
public class DruidConfiguration {
    @Value("${mydurid.username}")
    private String username;
    @Value("${mydurid.password}")
    private String password;

    @Bean
    public ServletRegistrationBean druidStatViewServle() {
        //注册服务
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(
                new StatViewServlet(), "/druid/*");
        // 白名单(为空表示,所有的都可以访问,多个IP的时候用逗号隔开)
        servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
        // IP黑名单 (存在共同时，deny优先于allow)
        servletRegistrationBean.addInitParameter("deny", "127.0.0.2");
        // 设置登录的用户名和密码
        servletRegistrationBean.addInitParameter("loginUsername", username);
        servletRegistrationBean.addInitParameter("loginPassword", password);
        // 是否能够重置数据.
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean druidStatFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(
                new WebStatFilter());
        // 添加过滤规则
        filterRegistrationBean.addUrlPatterns("/*");
        // 添加不需要忽略的格式信息
        filterRegistrationBean.addInitParameter("exclusions",
                "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        System.out.println("druid初始化成功!");
        return filterRegistrationBean;

    }
}

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
52
53
因为此处测试采用的是mybatis框架的动态mapper来进行持久化操作，所以需要在配置文件中指定mapper文件位置，我的在resource目录下新建了一个mapper文件，并且配置了。

package com.dsk.dsk_xcx;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient//注册到eureka
@MapperScan("com.dsk.dsk_xcx.dao")//mybatis动态代理的mapper包
public class DskXcxApplication {

    public static void main(String[] args) {
        SpringApplication.run(DskXcxApplication.class, args);
        System.out.println("启动成功......");
    }

}

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
测试生成服务心得
1,增加了druid监控系统，让我们可以清楚看到哪些慢sql以及连接池当前状态信息等。从此节省了我们定位慢sql的时间。
2，因为开启监控所以需要重新对其进行配置。
3，访问方式当前服务ip+port/durid/index.html
1
2
3
搭建测试消费服务
pom文件如下:

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.4.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.dsj</groupId>
    <artifactId>dsk_consumer</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>dsk_consumer</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Finchley.SR1</spring-cloud.version>
    </properties>

    <dependencies>
        <!--boot config start-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.0.0</version>
        </dependency>
        <!--boot config end-->

        <!--cloud config start-->
        <!--eureka客户端依赖-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <!--feign依赖-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
        <!--hystrix依赖，主要是用  @HystrixCommand-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>
        <!--cloud config end-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        <!-- alibaba的druid数据库连接池 -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.9</version>
        </dependency>
        <!--dashborad需要-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
52
53
54
55
56
57
58
59
60
61
62
63
64
65
66
67
68
69
70
71
72
73
74
75
76
77
78
79
80
81
82
83
84
85
86
87
88
89
90
91
92
93
94
95
96
97
配置文件如下:

eureka:
  client:
    serviceUrl:
      defaultZone: http://dsj:123456@localhost:8761/eureka
  instance:
    instance-id: ${spring.cloud.client.ip-address}:${server.port}
    prefer-ip-address: true #显示ip
server:
  port: 8080
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    druid:
          filters: stat # 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
          driver-class-name: com.mysql.jdbc.Driver
          url: jdbc:mysql://***:3306/testcloud?useUnicode=true&characterEncoding=utf-8
          username: root
          password: ***
          #配置初始化大小/最小/最大
          initialSize: 1
          minIdle: 1
          maxActive: 20
          #获取连接等待超时时间
          maxWait: 60000
          #间隔多久进行一次检测，检测需要关闭的空闲连接
          timeBetweenEvictionRunsMillis: 60000
          #一个连接在池中最小生存的时间
          minEvictableIdleTimeMillis: 300000
#开始配置mybatis
mybatis:
  mapper-locations: classpath:mapper/*.xml #指定mapper文件地址
  type-aliases-package: com.dsj.dsk_consumer.modle #指定别名包
logging:
  level:
    com.codingapi.txlcn: debug #配置com.dsj.dsk_consumer包下面所有类以debug日志级别输出
mydurid: #配置druid的监控页面安全认证
  username: admin
  password: 123456
#修改调用超时时间,feign
feign:
  hystrix:
    enabled: true
  client:
    config:
      default:
        connectTimeout: 3000
        readTimeout: 3000
#设置超时时间,hystrix
hystrix:
  command:
    default:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 3000
#暴露全部的监控信息
management:
  endpoints:
    web:
      exposure:
        include: "*"

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
52
53
54
55
56
57
58
59
60
61
62
同样开起了连接池监控后台:需要重复生产者操作。

package com.dsj.dsk_consumer;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient//eureka客户端
@MapperScan("com.dsj.dsk_consumer.dao")//mybatis扫描mapper包
@EnableCircuitBreaker//Hystrix
@EnableFeignClients(basePackages = "com.dsj.dsk_consumer.feign")//开启feign，注意一定要指定包，要不然会报错，版本原因
public class DskConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DskConsumerApplication.class, args);
    }
}

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
feign和断路器的使用:

package com.dsj.dsk_consumer.feign;

import com.dsj.dsk_consumer.modle.Test;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * @author 彭伟
 * @Date 2019/4/3 13:58
 * api接口
 */
@FeignClient(name = "dsk-xcx", fallback = HystrixClientFallback.class)
//name中是被调用方的spring.application.name,fallback断路器返回结果
public interface TestFeignClient {
    @RequestMapping(value = "/test/findAll", method = RequestMethod.GET)
    Object findAll();

    //    consumes = "application/json"
    @RequestMapping(value = "/test/add", method = RequestMethod.POST,consumes = "application/json")
    String insert(@RequestBody Test test);
}

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
feign回滚操作，需要实现feign接口并重写方法

package com.dsj.dsk_consumer.feign;


import com.dsj.dsk_consumer.modle.Test;
import org.springframework.stereotype.Component;


/**
 * @author 彭伟
 * @Date 2019/4/3 14:16
 */
@Component
public class HystrixClientFallback implements TestFeignClient {
    @Override
    public Object findAll() {
        return "失败";
    }

    @Override
    public String insert(Test test) {
        return "失败";
    }
}

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
最后在断路器中进行异常通知操作:

 @GetMapping("/findAll")
    @HystrixCommand(fallbackMethod = "findAllFail")
    public Object findAll(HttpServletRequest request) {
        return feignClient.findAll();
    }

    //注意，方法签名一定要要和api方法一致
    private Object findAllFail(HttpServletRequest request) {
        //监控报警
        String saveOrderKye = "dsk-xcx";
        String sendValue = "";
//        String sendValue = redisTemplate.opsForValue().get(saveOrderKye);
        final String ip = request.getRemoteAddr();
        new Thread(() -> {
            if (StringUtils.isBlank(sendValue)) {
                System.out.println("紧急短信，调用失败，请离开查找原因,ip地址是=" + ip);
                //发送一个http请求，调用短信服务 TODO
//                redisTemplate.opsForValue().set(saveOrderKye, "save-order-fail", 20, TimeUnit.SECONDS);

            } else {
                System.out.println("已经通知开发人员，20秒内不重复发送");
            }

        }).start();
        Map<String, Object> msg = new HashMap<>();
        msg.put("code", -1);
        msg.put("msg", "访问人数太多，您被挤出来了，稍等重试");
        return msg;
    }
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
搭建消费者是的心得
1，因为消费者端集成了Hystrix和feign等组件，但是因为cloud版本原因需要注意maven坐标以及配置问题。
2，同时配置文件特别需要注意的是feign和Hystrix都需要配置超时时间，建议配成一样的，如果不配成一样的会以Hystrix为准。
3，因为springcloud的ioc容器是父子容器，意味着feign包下面的类其实又是另一个ioc容器，所以当前版本不需要在启动类外层目录下创建新的包，但是需要指定包名。
1
2
3
网关zuul搭建步骤:
pom文件:

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.4.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.dsj</groupId>
    <artifactId>dsk_zuul</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>dsk_zuul</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Finchley.SR1</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <!--引入zuul服务依赖-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
52
53
54
55
56
57
58
59
60
61
配置文件如下:

eureka:
  client:
    serviceUrl:
      defaultZone: http://dsj:123456@localhost:8761/eureka
  instance:
    instance-id: ${spring.cloud.client.ip-address}:${server.port}
    prefer-ip-address: true #显示ip
server:
  port: 8002
spring:
  servlet:
    multipart:
      max-file-size: 100Mb
      max-request-size: 100Mb
ribbon:
    ReadTimeout: 5000
    ConnectTimeout: 5000
zuul:
  routes:
    api-a:
      path: /api-xcx/**
      service-id: dsk-xcx
    api-b:
      path: /api-cu/**
      service-id: dsk-consumer
#处理请求头中敏感信息被过滤问题,放空就不会导致下游服务取不到cookie问题
  sensitive-headers:
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
启动类:

package com.dsj.dsk_zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableZuulProxy//zuul
//@EnableEurekaClient //zull里面已经集成了
@SpringBootApplication
public class DskZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(DskZuulApplication.class, args);
    }

}

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
限流过滤器:

package com.dsj.dsk_zuul.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * @author 彭伟
 * @Date 2019/4/8 10:41
 * 限流
 */
@Component
public class CurrentFilter extends ZuulFilter {
    //每秒产生1000个令牌,根据令牌来进行限流
    private static final RateLimiter RATE_LIMITER = RateLimiter.create(1);

    @Override
    public String filterType() {
        return PRE_TYPE;//采用前置过滤
    }

    @Override
    public int filterOrder() {
        return -4;//优先级,因为限流采用最高优先级,越小优先级越高
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        //获取当前请求头信息，对需要过滤的服务进行限流
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        if (!RATE_LIMITER.tryAcquire()) {//没有获取到令牌，不放行并且返回响应结果,可自定义
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpStatus.TOO_MANY_REQUESTS.value());
            requestContext.setResponseBody("当前访问人数过多，您被挤出来了");
            requestContext.getResponse().setContentType("application/json;charset=UTF-8");
        }
        return null;
    }
}

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
52
53
54
zuul心得
1，其实就是统一了入口，需要注意的是写filter必须继承zuulfilter，需要指定前置还是后置过滤器。
2，同时需要注意的是因为内置对cookie等敏感信息屏蔽了，需要通过配置文件对其进行打开，详细配置请看配置文件。
3，限流需要经过准确的压测来进行综合性的评估，令牌桶算法会耗费一点资源，需要除去令牌桶算法的资源。
1
2
3
搭建配置中心服务端
pom文件:

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.4.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.dsj</groupId>
    <artifactId>dsk_config_server</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>dsk_config_server</name>
    <description>配置服务器</description>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <spring-cloud.version>Finchley.SR1</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
52
53
54
55
56
57
58
59
60
61
62
63
配置文件:

spring:
  application:
    name: config-server
  cloud:
    config:
      server:
        git:
          uri: https://gitee.com/itpengwei/cloud-config #仓库地址,注意去掉.git
          username:  #用户名
          password:  #密码
          timeout: 5 #超时时间，单位为s
          default-label: master #分支
server:
  port: 9100
eureka:
  client:
    serviceUrl:
      defaultZone: http://dsj:123456@localhost:8761/eureka
  instance:
        instance-id: ${spring.cloud.client.ip-address}:${server.port}
        prefer-ip-address: true 
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
启动类:

package com.dsj.dsk_config_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class DskConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DskConfigServerApplication.class, args);
    }

}

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
客户端使用，例如我这用zuul来示范
注意事项1,配置文件一定要改名成bootstrap.yml这样才能优先加载,其他配置可以全部放仓库

eureka:
  client:
    serviceUrl:
      defaultZone: http://dsj:123456@localhost:8761/eureka
  instance:
    instance-id: ${spring.cloud.client.ip-address}:${server.port}
    prefer-ip-address: true #显示ip
#服务的名称
spring:
  application:
    name: dsk-zuul
  #指定从哪个配置中心读取
  cloud:
    config:
      discovery:
        service-id: config-server
        enabled: true
      #建议用lable去区分环境，默认是lable是master分支
      label: master

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
仓库配置文件名称和服务名称一致既可；
pom文件加入如下依赖:

<!--配置中心客户端-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-client</artifactId>
        </dependency>
1
2
3
4
5
到此就完成了
config客户端从服务端拉去配置的操作。

config的心得
1,服务端一定要能够连接到远程仓库。
2，需要在客户端启动之前启动。
3，仓库的uri需要去掉后缀.git
4，服务端和客户端都需要指定从哪个分支拉取配置。
5，仓库的配置文件最好和服务名一致。
6，客户端一定要把配置文件改名成bootstrap.yml，让spring先对里面配置进行加载。
1
2
3
4
5
6
搭建dashboad监控服务
pom文件如下:

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.4.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.dsj</groupId>
    <artifactId>dsk_hystrix_dashboard</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>dsk_hystrix_dashboard</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Finchley.SR1</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
52
53
54
55
56
57
配置文件:

server:
  port: 8900
1
2
启动类；

package com.dsj.dsk_hystrix_dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableHystrixDashboard//开启自动dashboard的自动装配，适用于单机，集群不适合
public class DskHystrixDashboardApplication {
//    http://localhost:8781/hystrix
//    http://192.168.25.1:8080/actuator/hystrix.stream
    public static void main(String[] args) {
        SpringApplication.run(DskHystrixDashboardApplication.class, args);
    }

}

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
被监控的服务需要满足以下条件

1,消费了其他服务
2，pom文件需要添加依赖
<!--dashborad需要-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
3，然后启动监控服务浏览器输入http://localhost:8900/hystrix
4,输入消费者服务的地址格式如下:
http://192.168.25.1:8080/actuator/hystrix.stream
5，将链接放入最大的那个框，点击监控，既可与被监控的服务建立连接
1
2
3
4
5
6
7
8
9
10
11
dashborad注意事项
1,监控的一定要是一个消费者，被监控者一定要添加上面的mavne依赖。
2，想看到监控数据一定要先访问那个被监控服务的接口，那个接口一定要是调用了其他服务。
3，监控的是实时数据，具体多少秒内可以在可视化界面第二行最左边那个框里面输入，单位是ms。
4，建议采用默认，因为监控同时也在耗费那个被监控的服务器资源。
1
2
3
4
集成分布式事务协调框架tx-lcn
第一步先搭建tx-mange事务管理器
pom文件如下:

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <!--<version>2.1.0.RELEASE</version>-->
        <version>2.0.4.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.dsk</groupId>
    <artifactId>tx-manger</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>tx-manger</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Finchley.SR1</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.0.0</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.codingapi.txlcn</groupId>
            <artifactId>txlcn-tm</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>
        <!-- alibaba的druid数据库连接池 -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.9</version>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
52
53
54
55
56
57
58
59
60
61
62
63
64
65
66
67
68
69
70
71
72
73
74
75
76
77
78
79
80
81
82
83
84
85
86
87
88
89
90
配置文件如下:

spring.application.name=tx-manager
server.port=7970

spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://211.159.169.156:3306/tx-manager?useUnicode=true&characterEncoding=utf-8
spring.datasource.username=root
spring.datasource.password=dsj123456
#指定注册中心地址
eureka.client.serviceUrl.defaultZone= http://dsj:123456@localhost:8761/eureka
eureka.instance.instance-id=${spring.cloud.client.ip-address}:${server.port}
eureka.instance.prefer-ip-address=true

mybatis.configuration.map-underscore-to-camel-case=true
mybatis.configuration.use-generated-keys=true

# TxManager Host Ip
tx-lcn.manager.host=127.0.0.1
# TxClient连接请求端口
tx-lcn.manager.port=5800
# 心跳检测时间(ms)
tx-lcn.manager.heart-time=15000
# 分布式事务超时时间
tx-lcn.manager.dtx-time=5000
#参数延迟删除时间单位ms
tx-lcn.message.netty.attr-delay-time=10000
# 事务处理并发等级. 默认为机器逻辑核心数5倍
tx-lcn.manager.concurrent-level=128
# 开启日志,默认为false
tx-lcn.logger.enabled=true
logging.level.com.codingapi=debug
tx-lcn.logger.driver-class-name=${spring.datasource.driver-class-name}
tx-lcn.logger.jdbc-url=${spring.datasource.url}
tx-lcn.logger.username=${spring.datasource.username}
tx-lcn.logger.password=${spring.datasource.password}

#redis 主机
spring.redis.host=***
#redis 端口
spring.redis.port=6379
#redis 密码
spring.redis.password=***
tx-lcn.manager.admin-key=123456
##发生异常发送邮件给管理员
#spring.mail.host=smtp.126.com
#spring.mail.port=25
#spring.mail.username=pw1914109147@126.com
#spring.mail.password=
# 异常回调开关。开启时请制定ex-url
#tx-lcn.manager.ex-url-enabled=true
# 事务异常通知（任何http协议地址。未指定协议时，为TM提供内置功能接口）。默认是邮件通知
#tx-lcn.manager.ex-url=/provider/email-to/1914109147@qq.com
#tx-lcn.manager.ex-url=http://192.168.25.1:8002/api-cu/test/findAll
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
52
启动类；

package com.dsk.txmanger;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.codingapi.txlcn.tm.config.EnableTransactionManagerServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EnableTransactionManagerServer
//@EnableAutoConfiguration(exclude = {DruidDataSourceAutoConfigure.class})
public class TxMangerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TxMangerApplication.class, args);
    }

}

1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
tx-manger注意事项
1，pom文件最好延用上述的，要不然可能产生版本冲突，以上版本都是经过严格测试得出的。
2，配置文件里面的服务名称不能改动。
3，事务消息端口不要与其他服务端口冲突。
1
2
3
客户端使用
pom文件加入如下依赖:

<!--lcn事务客户端开始-->
        <dependency>
            <groupId>com.codingapi.txlcn</groupId>
            <artifactId>txlcn-tc</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>com.codingapi.txlcn</groupId>
            <artifactId>txlcn-txmsg-netty</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>
        <!--lcn事务客户端结束-->
1
2
3
4
5
6
7
8
9
10
11
12
13
配置文件加入如下配置:

#lcn事务管理器ip端口
tx-lcn:
  client:
    manager-address: 127.0.0.1:5800 #tm集群的情况下可以写多个地址，逗号隔开
#  ribbon:
#    loadbalancer:
#      dtx:
#        enabled: true #是否采用轮训


1
2
3
4
5
6
7
8
9
10
启动类加上注解:@EnableDistributedTransaction
最后事务发起方A调用了事务参与方B，那么两者都需要加上

@LcnTransaction//分布式事务
@Transactional//本地事务注解
1
2
分布式事务客户端注意事项
1,service层里面一定不要用一个try把几个db操作都给包起来，可以把try放到controller里面去。要不然会造成事务不回滚，框架会认为没有抛出事务。
2，配置的事务管理器ip一定要和tm的一致，端口是事务消息端口，并不是服务端口。
3，集群配置在配置文件里面有详细说明。
1
2
3
参考资料
tx-lcn管网

tx-lcn的github社区

dubbo和springcloud对比

注册中心eureka和其他组件资料

ribbon自定义资料

Hystrix资料

微服务系列文章-方志鹏

微服务系列-纯洁的微笑

微服务系列-江南一点雨
————————————————
版权声明：本文为CSDN博主「长流仙山拟画人」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/pw191410147/article/details/95494813