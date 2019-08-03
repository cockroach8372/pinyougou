package com.itheima.ssm.controller;

import com.itheima.ssm.SysLog;
import com.itheima.ssm.service.ISysLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@Component
@Aspect
public class logAop {
    @Autowired
    private HttpServletRequest request;
    private Date visitTime;
    private Class clazz;
    private Method method;

    @Autowired
    private ISysLogService iSysLogService;
    @Before("execution(*  com.itheima.ssm.controller.*.*(..))")
    public void doBefore(JoinPoint jp) throws NoSuchMethodException {
         visitTime=new Date();
         clazz=jp.getTarget().getClass();
        String  methodName=jp.getSignature().getName();
        Object[] args = jp.getArgs();
        if (args==null ||args.length==0 ){
            method = clazz.getMethod(methodName);
        }else {
            Class[] classArgs=new Class[args.length];
            for (int i = 0; i <classArgs.length ; i++) {
                 classArgs[i]=args[i].getClass();
            }
            clazz.getMethod(methodName,classArgs);
        }

    }
    @AfterReturning("execution(* com.itheima.ssm.controller.*.*(..))")
    public void doAfter(JoinPoint jp) throws Exception{
          long time=new Date().getTime()-visitTime.getTime();
          String url="";
          if (clazz!=null &&method!=null&&clazz!= logAop.class){
              RequestMapping classAnnotation = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
              if (classAnnotation!=null){
                  String[] classValue = classAnnotation.value();
                  RequestMapping methodAnnotation = (RequestMapping) method.getAnnotation(RequestMapping.class);
                  if (methodAnnotation!=null){
                      String[] methodValue = methodAnnotation.value();
                      url=classValue[0]+methodValue[0];
                  }
              }
          }
        String ip = request.getRequestURI();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SysLog sysLog = new SysLog();
        sysLog.setExecutionTime(time);
        sysLog.setIp(ip);
        sysLog.setMethod("[类名]" + clazz.getName() + "[方法名]" + clazz.getName());
        sysLog.setUrl(url);
        sysLog.setUsername(username);
        sysLog.setVisitTime(visitTime);
        iSysLogService.save(sysLog);
    }
}
