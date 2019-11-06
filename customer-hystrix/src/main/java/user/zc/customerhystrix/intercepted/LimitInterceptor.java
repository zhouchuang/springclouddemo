package user.zc.customerhystrix.intercepted;


import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class LimitInterceptor {
    private Logger logger = LoggerFactory.getLogger(LimitInterceptor.class);
    private RateLimiter rateLimiter;
    private ConcurrentHashMap<String, RateLimiter> map = new ConcurrentHashMap<>();


    @Autowired
    private HttpServletResponse httpServletResponse;

    /**
     * @Description: 定义切入点
     * @Title: pointCut
     * @author OnlyMate
     * @Date 2018年9月10日 下午3:52:17
     */
    @Pointcut("execution(public * user.zc.customerhystrix.controller.*.*(..)) && @annotation(user.zc.customerhystrix.intercepted.Limit)")
    public void pointCut(){
    }


    /**
     * @Description: 后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
     * @Title: after
     * @author OnlyMate
     * @Date 2018年9月10日 下午3:52:48
     * @param jp
     */
    @After("pointCut()")
    public void after(JoinPoint jp){
        logger.info("【注解：After】方法最后执行.....");
    }

    /**
     * @Description: 环绕通知,环绕增强，相当于MethodInterceptor
     * @Title: around
     * @author OnlyMate
     * @Date 2018年9月10日 下午3:52:56
     * @param pjp
     * @return
     */
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) {
        Object obj = null;
        logger.info("【注解：Around . 环绕前】方法环绕start.....");
        try {
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            Method method = signature.getMethod();
            Limit limit = method.getAnnotation(Limit.class);
            String name = limit.name();
            if(StringUtils.isEmpty(name)){
                name = method.getName();
            }
            int limitNum = limit.limitNum();
            if(map.containsKey(name)){
                rateLimiter = map.get(name);
            }else{
                rateLimiter = RateLimiter.create(limitNum);
                map.put(name,rateLimiter);
            }
            try{
                if(rateLimiter.tryAcquire()){
                    //如果不执行这句，会不执行切面的Before方法及controller的业务方法
                    obj =  pjp.proceed();
                    logger.info("【注解：Around. 环绕后】方法环绕proceed，结果是 :" + obj);
                }else{
                    logger.error("拒绝了请求");
                    httpServletResponse.setContentType("application/json;charset=UTF-8");
                    try (
                        ServletOutputStream outputStream = httpServletResponse.getOutputStream()) {
                        outputStream.write("请求量过高，拒绝了请求".getBytes("utf-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }catch (Throwable throwable){
                throwable.printStackTrace();
            }


        } catch (Throwable e) {
            e.printStackTrace();
        }
        return obj;
    }


    /**
     * @Description: 后置返回通知
     * @Title: afterReturning
     * @author OnlyMate
     * @Date 2018年9月10日 下午3:52:30
     * @param ret
     * @throws Throwable
     */
    @AfterReturning(returning = "ret", pointcut = "pointCut()")
    public void afterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        logger.info("【注解：AfterReturning】这个会在切面最后的最后打印，方法的返回值 : " + ret);
    }

    /**
     * @Description: 后置异常通知
     * @Title: afterThrowing
     * @author OnlyMate
     * @Date 2018年9月10日 下午3:52:37
     * @param jp
     */
    @AfterThrowing("pointCut()")
    public void afterThrowing(JoinPoint jp){
        logger.info("【注解：AfterThrowing】方法异常时执行.....");
    }

    /**
     * @Description: 定义前置通知
     * @Title: before
     * @author OnlyMate
     * @Date 2018年9月10日 下午3:52:23
     * @param joinPoint
     * @throws Throwable
     */
    @Before("pointCut()")
    public void before(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        logger.info("【注解：Before】------------------切面  before");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        logger.info("【注解：Before】浏览器输入的网址=URL : " + request.getRequestURL().toString());
        logger.info("【注解：Before】HTTP_METHOD : " + request.getMethod());
        logger.info("【注解：Before】IP : " + request.getRemoteAddr());
        logger.info("【注解：Before】执行的业务方法名=CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("【注解：Before】业务方法获得的参数=ARGS : " + Arrays.toString(joinPoint.getArgs()));

    }




}
