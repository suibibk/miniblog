package com.suibibk.aop;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.suibibk.annotation.LoginCheck;
import com.suibibk.utils.Const;
/*
 * 使用AOP统一处理权限和日志
 * @author forever
 */
@Aspect
@Component
@Order(1)
public class MgrAspect {

	private static final Logger log= LoggerFactory.getLogger(MgrAspect.class);
	@Pointcut("execution(public * com.suibibk.controller.*.*(..))")
	public void web() {
	}

	@Around("web()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		Method realMethod = getControllerMethod(pjp);
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String requestType = request.getHeader("X-Requested-With");
		LoginCheck loginCheck = realMethod.getDeclaredAnnotation(LoginCheck.class);
		if(loginCheck!=null) {
			log.debug("需要進行登陸校驗");
			//先检查是否登录，要进行权限检查的必定是已经登录了的
			HttpSession session  =request.getSession();
	        //1、判断session中有没有user信息，若是有，则表明用户登录成功，直接转到首页,否则跳转到登录界面
	        String  mgruserid = (String) session.getAttribute("mgruserid");
			
	        if(StringUtils.isBlank(mgruserid)) {
	            //XMLHttpRequest这个表示异步
	            log.debug("requestType:"+requestType);
	            if(requestType!=null&&"XMLHttpRequest".equals(requestType)) {
	                Map<String,Object> msg = new HashMap<String,Object>();
	                msg.put("code", "1");
	                msg.put("msg", "请先登录");
	                return msg;
	            }else {
	            	return "redirect:/mgr/login";
	            	
	            	
	            	
	            	//这里先借助第三方登录，到时候再移动过来
	            	
	            	 //暂时来说，直接这里重定向走比较好，不要用户再去点击，等有其他登录方式的时候再跳转到登录页面
					/*
					 * String scheme = request.getScheme(); String basePath =
					 * scheme+"://"+request.getServerName(); String redirect_uri =
					 * basePath+"/sso/login"; //转码 log.info("redirect_uri:"+redirect_uri); try {
					 * redirect_uri = URLEncoder.encode(redirect_uri, "UTF-8"); } catch
					 * (UnsupportedEncodingException e) { e.printStackTrace(); } String redirect
					 * ="https://www.suibibk.com/login?link="+redirect_uri+"&user_type=01"; return
					 * "redirect:"+redirect;
					 */
	            }
	        }
			
		}
		
		return pjp.proceed();
	}
	

	private Method getControllerMethod(ProceedingJoinPoint pjp) throws NoSuchMethodException, SecurityException {
		Signature signature = pjp.getSignature();    
		MethodSignature methodSignature = (MethodSignature)signature;    
		Method targetMethod = methodSignature.getMethod();  
		String name = signature.getName();
		log.debug("进入权限AOP的方法名称："+name);
		Method realMethod = pjp.getTarget().getClass().getDeclaredMethod(signature.getName(), targetMethod.getParameterTypes());
		return realMethod;
	}
}
