package io.inforzia.todolist;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Aspect
@Component
public class HeaderAspect {
    static String userHeader;
    @Before("execution(* io.inforzia.todolist.TodoController.*(..))")
    public void getHeader(){
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        userHeader = req.getHeader("authorization");
        log.info(userHeader + " 로그인~~");
    }
}
