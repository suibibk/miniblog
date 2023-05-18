package com.suibibk.utils;

import java.util.HashMap;
import java.util.Map;

import javax.swing.CellEditor;

import org.apache.commons.lang.StringUtils;

/**
 * 自定义session，我们这里session默认有效期也为两个钟
 * @author forever
 *
 */
public class MySession {
	/**
	 * 获取session中的内容
	 * @param key
	 * @return
	 */
	private static Map<String,Object>  getSession(String token) {
		//根据token去获取用户信息
		Map<String,Object> map = (Map<String, Object>) Cache.getValue(token);
		if(map==null) {
			return null;
		}
		//若不为空，则获取map中的时间字段，看看有没有过期
		Long createTime = (Long) map.get("createTime");
		if(createTime==null) {
			createTime=0l;
		}
		//获取当前时间
		Long nowTime = System.currentTimeMillis();
		//如果大于两个钟，则清除改key，这里不需要防并发，最多清理不到
		if((nowTime-createTime)>2*24*60*60*1000) {
			Cache.remove(token);
			return null;
		}
		return map;
	}
	/**
	 * 获取session中的内容
	 * @param key
	 * @return
	 */
	public static Object getSessionValue(String token,String key) {
		if(StringUtils.isBlank(token)||StringUtils.isBlank(key)) {
			return null;
		}
		//根据token去获取用户信息
		Map<String,Object> map = MySession.getSession(token);
		if(map!=null) {
			return map.get(key);
		}
		return null;
	}
	/**
	 * 设置值
	 * @param token
	 * @param key
	 * @param value
	 */
	public static void setSession(String token,String key,Object value) {
		//根据token去获取用户信息
		Map<String,Object> map = (Map<String, Object>) Cache.getValue(token);
		if(map==null) {
			map = new HashMap<String, Object>();
			map.put("createTime", System.currentTimeMillis());
		}else {
			//有操作session则重新初始化时间
			map.put("createTime", System.currentTimeMillis());
		}
		map.put(key, value);
		Cache.setValue(token, map);
	}
	
	public static void main(String[] args) {
		/*MySession.setSession("1", "openid", "1231");
		System.out.println(MySession.getSession("1").toString());
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MySession.setSession("1", "aaa", "1231");
		System.out.println(MySession.getSession("1").toString());*/
		
	}
	
}
