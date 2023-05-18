package com.suibibk.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.suibibk.entity.Menu;

public class Cache {
	private final static Map<String,Object> map= new ConcurrentHashMap<String, Object>();
	
	public static Object getValue(String key) {
		Object value = map.get(key);
		return value;
	}
	
	public static void setValue(String key,Object Object) {
		map.put(key, Object);
	}
	public static void remove(String key) {
		map.remove(key);
	}
	
	public static void main(String[] args) {
		List<Menu> menus = new ArrayList<Menu>();
		Menu menu = new Menu();
		menu.setId("1");
		menus.add(menu);
		Cache.setValue("A", menus);
		
		List<Menu> menus2 = (List<Menu>) Cache.getValue("A");
		System.out.println(menus);
		Menu menu2 = new Menu();
		menu2.setId("2");
		
		menus2.add(menu2);
		
		System.out.println(menus2);
		
		List<Menu> menus3 = (List<Menu>) Cache.getValue("A");
		System.out.println(menus3);
	}
}
