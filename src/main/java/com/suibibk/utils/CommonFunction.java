package com.suibibk.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.SynthesizedAnnotation;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonFunction {
    private static final Logger log = LoggerFactory.getLogger(CommonFunction.class);
    private static final String[] images =new String[]{"jpg", "jpeg", "gif", "png", "bmp","mp3"};
    public static Object  mapToEntity(Map<String,Object> map,Class clazz){
        try {
            if(map!=null){
                Object obj = clazz.newInstance();
                Field[] fs =  clazz.getDeclaredFields();
                for(Field field:fs){
                    String name =  field.getName();
                    Object value = map.get(name);
                    if(value!=null){
                        field.setAccessible(true);
                        if(value.getClass().equals(Integer.class)){
                            //System.out.println("是整形");
                            value = Long.parseLong(value+"");
                        }
                        field.set(obj,value);
                    }
                }
                return obj;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static List listMapToListEntity(List<Map<String,Object>> maps, Class clazz){
        try{
            List<Object> list = new ArrayList<Object>();
            for (Map<String,Object> map:maps) {
                Object obj = clazz.newInstance();
                Field[] fs =  clazz.getDeclaredFields();
                for(Field field:fs){
                    String name =  field.getName();
                    Object value = map.get(name);
                    if(value!=null){
                        field.setAccessible(true);
                        if(value.getClass().equals(Integer.class)){
                            System.out.println("是整形");
                            value = Long.parseLong(value+"");
                        }
                        field.set(obj,value);
                    }
                }
                list.add(obj);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 检查图片格式是否正确
     * @param suffix
     * @return
     */
    public static Boolean checkImage(String suffix){
        log.info("suffix:"+suffix);
        suffix=suffix.toLowerCase();
        for(String str:images){
            if(str.equals(suffix)){
                return true;
            }
        }
        return false;
    }

    public static String zhuanyi(String content){
        content= content.replaceAll("<","&lt;");
        content= content.replaceAll(">","&gt;");
        return content;
    }
    
    /**
     *  	随机获取下标
     * @param num
     * @param total
     * @return
     */
    public static int[] getInts(int num,int total) {
    	//随机数
    	int[] indexs = new int[num];
    	List<Integer> list = new ArrayList<Integer>();
    	for (int i = 0; i < total; i++) {
    		list.add(i);
    	}
    	SecureRandom random = new SecureRandom();
    	for (int i = 0; i < num; i++) {
    		int index = random.nextInt(total);
    		indexs[i] = list.get(index);
    		list.remove(index);
    		total--;
    	}
    	for (int i = 0; i < num; i++) {
    		System.out.print(indexs[i]+",");
    	}
    	return indexs;
    }
	
}
