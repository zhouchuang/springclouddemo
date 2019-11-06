package user.zc.customerhystrix.intercepted;


import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Limit {
    String name() default "";
    int period() default 1;  //1秒
    int count() default 10; //10次
    int limitNum() default 10;
}
