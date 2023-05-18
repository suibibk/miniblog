package com.suibibk.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.suibibk.entity.Focal;
import com.suibibk.entity.Menu;
import com.suibibk.entity.SetUp;
import com.suibibk.entity.Content;
import com.suibibk.entity.Topic;
import com.suibibk.entity.Visit;
import com.suibibk.utils.Cache;
import com.suibibk.utils.Const;
import com.suibibk.utils.SnowflakeIdUtil;
import com.suibibk.xml.XMLUtils;

@Component
public class CommonService {
	private static final Logger log = LoggerFactory.getLogger(CommonService.class);
	private static final ThreadLocal<SimpleDateFormat> format = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		};
	};
	@Value("${upload.path}")
	private String path = "E:/data";
    
	private  String getXMLPath(Class<?> clazz,String id) {
		if(clazz==Menu.class) {
			return path+"/table/menus/"+id+".xml";
		}
		if(clazz==Focal.class) {
			return path+"/table/focals/"+id+".xml";
		}
		if(clazz==Topic.class) {
			return path+"/table/topics/"+id+".xml";
		}
		if(clazz==Content.class) {
			return path+"/table/contents/"+id+".xml";
		}
		if(clazz==Visit.class) {
			return path+"/table/visits/"+id+".xml";
		}
		if(clazz==SetUp.class) {
			return path+"/table/setups/"+id+".xml";
		}
		return "";
	}
	private  String[] getFileNamePath(Class<?> clazz) {
		String filePath = "";
		if(clazz==Menu.class) {
			filePath = path+"/table/menus/";
		}
		if(clazz==Focal.class) {
			filePath = path+"/table/focals/";
		}
		if(clazz==Topic.class) {
			filePath = path+"/table/topics/";
		}
		if(clazz==Content.class) {
			filePath = path+"/table/contents/";
		}
		String[] list=new File(filePath).list();
		return list;
	}

   

    public List<Menu> getMenus(String visible) {
    	List<Menu> menus  = (List<Menu>) Cache.getValue(Const.MENUS_KEY);
    	if(menus==null) {
    		menus = new ArrayList<Menu>();
    		String[] names = getFileNamePath(Menu.class);
        	for (int i = 0; i < names.length; i++) {
    			String name = names[i];
    			String xmlPath = getXMLPath(Menu.class, name.replace(".xml", ""));
    			Menu menu = XMLUtils.select(Menu.class, xmlPath);
    			if(menu!=null) {
    				menus.add(menu);
    			}
    		}
    		Cache.setValue(Const.MENUS_KEY, menus);
    	}
    	
    	List<Menu> showMenus = new ArrayList<Menu>();
		for (Menu menu : menus) {
			 if(StringUtils.isNotBlank(visible)) {
				if(visible.equals(menu.getVisible())) {
					showMenus.add(menu);
				}
			}else {
				 showMenus.add(menu);
			 }
		}
		//排序
		Collections.sort(showMenus);
        return showMenus;
    }

    public Map<String,Object>  getTopics(String menuid, Integer page, Integer size, String visible) {
    	List<Topic> topics   = getTopics();
    	
       
        List<Topic> showTopics = new ArrayList<Topic>();
        //这里要倒序来添加
        for (Topic topic : topics) {
        	if(StringUtils.isNotBlank(visible)) {
        		if(!visible.equals(topic.getVisible())) {
        			continue;
        		}
        	}
        	if(StringUtils.isNotBlank(menuid)) {
        		if(menuid.equals(topic.getMenuid())) {
        			showTopics.add(topic);
        		}
        	}else {
        		showTopics.add(topic);
        	}
		}
        //先进行倒序，再分页
        Collections.sort(showTopics);
        //分页查询
        int num = showTopics.size();
        List<Topic> trueTopics = new ArrayList<Topic>();
        int start = (page-1)*size;
        int end = start+size;
        for (int i =start; i <num; i++) {
			if(i<end) {
				trueTopics.add(showTopics.get(i));
			}
		}
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("TOTAL", num);
        map.put("TOPICS", trueTopics);
        return map;
    }
    public  Map<String,Object>   searchTopics(String menuid, Integer page, Integer size, String visible,String value) {
    	List<Topic> topics   = getTopics();
    	List<Topic> showTopics = new ArrayList<Topic>();
    	
    	//这里要倒序来添加
    	for (Topic topic : topics) {
    		if(StringUtils.isNotBlank(visible)) {
        		if(!visible.equals(topic.getVisible())) {
        			continue;
        		}
        	}
    		if(topic.getTitle().contains(value)||topic.getDescription().contains(value)) {
    			showTopics.add(topic);
    		}
    	}
    	
    	//先进行倒序，再分页
        Collections.sort(showTopics);
    	//分页查询
    	int num = showTopics.size();
    	List<Topic> trueTopics = new ArrayList<Topic>();
    	int start = (page-1)*size;
    	int end = start+size;
    	for (int i =start; i <num; i++) {
    		if(i<end) {
    			trueTopics.add(showTopics.get(i));
    		}
    	}
    	
    	 Map<String,Object> map = new HashMap<String, Object>();
         map.put("TOTAL", num);
         map.put("TOPICS", trueTopics);
         return map;
    }
    public List<Focal> getFocals(String type,String visible) {
    	List<Focal> focals   = (List<Focal>) Cache.getValue(Const.FOCALS_KEY);
    	if(focals==null) {
    		focals  = new ArrayList<Focal>();
        	String[] names = getFileNamePath(Focal.class);
        	for (int i = 0; i < names.length; i++) {
    			String name = names[i];
    			String xmlPath = getXMLPath(Focal.class, name.replace(".xml", ""));
    			Focal focal = XMLUtils.select(Focal.class, xmlPath);
    			if(focal!=null) {
    				focals.add(focal);
    			}
    		}
        	Cache.setValue(Const.FOCALS_KEY,focals);
    	}
    	
    	
		 List<Focal> showFocals = new ArrayList<Focal>();
		 for (Focal focal : focals) {
			 if(type.equals(focal.getType())) {
				if(StringUtils.isNotBlank(visible)) {
					if(visible.equals(focal.getVisible())) {
						showFocals.add(focal);
					}
				}else {
					showFocals.add(focal);
				}
			 }
			
		 }
	     Collections.sort(showFocals);
	     return showFocals;
		 
	}
//下面是前端后台统一查询的方法
    
    public Map<String,Object> getTopic(String topicid, String visible) {
    	 Topic topic = (Topic) Cache.getValue("TOPIC_"+topicid);
    	 if(topic==null) {
    		 String topicXmlPath = getXMLPath(Topic.class, topicid);
    		 topic = XMLUtils.select(Topic.class, topicXmlPath);
    		 if(topic!=null) {
    			 Cache.setValue("TOPIC_"+topicid, topic);
    		 }
    	 }
    	
		 if(topic==null) {
			 return null;
		 }
		 if(StringUtils.isNotBlank(visible)) {
			 if(!visible.equals(topic.getVisible())) {
				 return null;
			 }
		 }
		 Content content = (Content) Cache.getValue("CONTENT_"+topicid);
		 if(content==null) {
			 String contentXmlPath = getXMLPath(Content.class, topicid);
			 content= XMLUtils.select(Content.class, contentXmlPath);
    		 if(content!=null) {
    			 Cache.setValue("CONTENT_"+topicid, content);
    		 }
    	 }
		 Map<String,Object> map = new HashMap<String,Object>();
		 map.put("TOPIC",topic);
		 map.put("CONTENT",content);
		 
		 //获取上一篇下一篇
		 if(StringUtils.isNotBlank(visible)) {
			Map<String,Object> preNextTopic = getPreNextTopic(topic);
			map.put("preNextTopic", preNextTopic);
		 }
         return map;
    }
    
    /**
     * 获取上一个下一个topic 上一篇id比较大
     * @param topic
     * @return
     */
    private Map<String,Object> getPreNextTopic(Topic newTopic){
    	 String id = newTopic.getId();
    	 String menuId = newTopic.getMenuid();
    	 String visible = newTopic.getVisible();
    	 List<Topic> showTopics = new ArrayList<Topic>();   	
    	 List<Topic> topics = getTopics();
		 //这里要倒序来添加
	     for (Topic topic : topics) {
	    	 if(visible.equals(topic.getVisible())&&menuId.equals(topic.getMenuid())) {
	    		 showTopics.add(topic);
	    	 }
	     }
	     Collections.sort(showTopics);
	     Topic preTopic = null;
	     Topic nextTopic = null;
	     for (int i = 0; i < showTopics.size(); i++) {
			if(showTopics.get(i).getId().equals(id)) {
				//这里就获取到当前这一篇文章了
				if(i>0) {
					preTopic = showTopics.get(i-1);
				}
				if(i+1<showTopics.size()) {
					nextTopic =showTopics.get(i+1);
				}
				break;
			}
		}
	    Map<String,Object> map = new HashMap<String,Object>();
	    if(preTopic!=null) {
	    	map.put("pre", "HAVE");
	    	map.put("preTopic",preTopic);
	    } 
	    if(nextTopic!=null) {
	    	map.put("next", "HAVE");
	    	map.put("nextTopic",nextTopic);
	    } 
	    return map;
    }
    /**
     * 获取topics
     * @return
     */
    private List<Topic> getTopics(){
    	//这里需要去获取上一篇和下一篇
		 List<Topic> topics   = (List<Topic>) Cache.getValue(Const.TOPICS_KEY);
	     if(topics==null) {
	    		topics  = new ArrayList<Topic>();
	    		String[] names = getFileNamePath(Topic.class);
	        	for (int i = 0; i < names.length; i++) {
	    			String name = names[i];
	    			String xmlPath = getXMLPath(Topic.class, name.replace(".xml", ""));
	    			Topic topic = XMLUtils.select(Topic.class, xmlPath);
	    			if(topic!=null) {
	    				topics.add(topic);
	    			}
	    		}
	        	Cache.setValue(Const.TOPICS_KEY,topics);
	     }
	     return topics;
	     
    }
    
    
    /*******************首图***************************/
	public Boolean addFocal(String focal_name, String focal_url, String focal_link,String sort,String type) {
		 try {
             String id = SnowflakeIdUtil.getSnowflakeId()+"";
             String xmlPath = this.getXMLPath(Focal.class, id);
             Focal focal = new Focal();
             focal.setFocal_url(focal_url);
             focal.setFocal_name(focal_name);
             focal.setFocal_link(focal_link);
             focal.setSort(sort);
             focal.setId(id);
             focal.setType(type);
             focal.setVisible("1");
             Boolean result = XMLUtils.save(focal, xmlPath);
             if(result) {
            	 List<Focal> focals   = (List<Focal>) Cache.getValue(Const.FOCALS_KEY);
            	 if(focals!=null) {
            		 //新增，这里就直接添加
            		 focals.add(focal);
            	 }
             }
             return result;
       }catch (Exception ex){
           log.error(ex.getMessage());
       }
       return false;
	}
	public Boolean updateFocal(String id,String focal_name, String focal_url, String focal_link,String sort,String type) {
		try {
			
			if(StringUtils.isNotBlank(id)) {
            	String xmlPath = this.getXMLPath(Focal.class, id);
            	Focal focal = XMLUtils.select(Focal.class,xmlPath);
                if(focal==null){
                   log.error("要修改的菜单不存在");
                   return false;
                }
                focal.setFocal_url(focal_url);
    			focal.setFocal_name(focal_name);
    			focal.setFocal_link(focal_link);
    			focal.setSort(sort);
    			focal.setId(id);
    			focal.setType(type);
                Boolean result = XMLUtils.update(focal,xmlPath);
                if(result) {
               	 List<Focal> focals   = (List<Focal>) Cache.getValue(Const.FOCALS_KEY);
               	 if(focals!=null) {
               		Iterator<Focal> itr = focals.iterator();
               		while(itr.hasNext()){
               		    if(itr.next().getId().equals(focal.getId())) {
               		    	itr.remove();
               		    }
               		}
               		focals.add(focal);
               	 }
                }
                return result;
            }
		}catch (Exception ex){
			log.error(ex.getMessage());
		}
		return false;
	}

	public Boolean visibleFocal(String id, String visible) {
		try {
        	String xmlPath = this.getXMLPath(Focal.class, id);
        	Focal focal = XMLUtils.select(Focal.class,xmlPath);
            if(focal!=null){
            	 if(!focal.getVisible().equals(visible)){
            		 focal.setVisible(visible);
                     Boolean result = XMLUtils.update(focal,xmlPath);
                     if(result) {
                       	 List<Focal> focals   = (List<Focal>) Cache.getValue(Const.FOCALS_KEY);
                       	 if(focals!=null) {
                       		Iterator<Focal> itr = focals.iterator();
                       		while(itr.hasNext()){
                       		    if(itr.next().getId().equals(focal.getId())) {
                       		    	itr.remove();
                       		    }
                       		}
                       		focals.add(focal);
                       	 }
                     }
                     return result;
                 }
            }
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return false;
	}


	public Boolean deleteFocal(String id) {
		try {
        	String xmlPath = this.getXMLPath(Focal.class, id);
            Boolean result = XMLUtils.delete(xmlPath);
            if(result) {
              	 List<Focal> focals   = (List<Focal>) Cache.getValue(Const.FOCALS_KEY);
              	 if(focals!=null) {
              		Iterator<Focal> itr = focals.iterator();
              		while(itr.hasNext()){
              		    if(itr.next().getId().equals(id)) {
              		    	itr.remove();
              		    }
              		}
              	 }
            }
            return result;
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return false;
	}
   





    


    
    
   
    

    /****************************菜单处理***********************************/
    public Boolean addMenu(String type, String name, String remark, String imgurl,String link, String sort,String parent_id) {
        try {
              String id = SnowflakeIdUtil.getSnowflakeId()+"";
              String xmlPath = this.getXMLPath(Menu.class, id);
              Menu menu = new Menu();
              menu.setId(id);
              menu.setName(name);
              menu.setRemark(remark);
              if(!"-1".equals(imgurl)){
                  menu.setImgurl(imgurl);
              }
              if(!"-1".equals(link)){
                  menu.setLink(link);
              }
              menu.setSort(sort);
              menu.setType(type);
              menu.setVisible("1");//默认可见;
              menu.setParent_id(parent_id);
              Boolean result = XMLUtils.save(menu, xmlPath);
              
              if(result) {
             	 List<Menu> menus   = (List<Menu>) Cache.getValue(Const.MENUS_KEY);
             	 if(menus!=null) {
             		 //新增，这里就直接添加
             		menus.add(menu);
             	 }
              }
              return result;
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return false;
    }
    public Boolean updateMenu(String menuid, String type, String name, String remark, String imgurl,String link, String sort,String parent_id) {
        try {
            if(StringUtils.isNotBlank(menuid)) {
            	String xmlPath = this.getXMLPath(Menu.class, menuid);
            	Menu menu = XMLUtils.select(Menu.class,xmlPath);
                if(menu==null){
                   log.error("要修改的菜单不存在");
                   return false;
                }
                menu.setName(name);
                menu.setRemark(remark);
                if(!"-1".equals(imgurl)){
                    menu.setImgurl(imgurl);
                }
                if(!"-1".equals(link)){
                    menu.setLink(link);
                }
                menu.setSort(sort);
                menu.setType(type);
                menu.setParent_id(parent_id);
                Boolean result = XMLUtils.update(menu,xmlPath);
                if(result) {
                	 List<Menu> menus   = (List<Menu>) Cache.getValue(Const.MENUS_KEY);
                	 if(menus!=null) {
                		 Iterator<Menu> itr = menus.iterator();
                    		while(itr.hasNext()){
                    		    if(itr.next().getId().equals(menu.getId())) {
                    		    	itr.remove();
                    		    }
                    		}
                    		menus.add(menu);
                	 }
                 }
                return result;
            }
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return false;
    }
    
    public Boolean deleteMenu(String menuid) {
        try {
        	String xmlPath = this.getXMLPath(Menu.class, menuid);
            Boolean result = XMLUtils.delete(xmlPath);
            if(result) {
           	 List<Menu> menus   = (List<Menu>) Cache.getValue(Const.MENUS_KEY);
           	 if(menus!=null) {
           		 Iterator<Menu> itr = menus.iterator();
               		while(itr.hasNext()){
               		    if(itr.next().getId().equals(menuid)) {
               		    	itr.remove();
               		    }
               		}
           	 }
            }
            return result;
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return false;
    }
    
    public Boolean visibleMenu( String menuid, String visible) {
        try {
        	String xmlPath = this.getXMLPath(Menu.class, menuid);
        	Menu menu = XMLUtils.select(Menu.class,xmlPath);
            if(menu!=null){
            	 if(!menu.getVisible().equals(visible)){
                     menu.setVisible(visible);
                     Boolean result = XMLUtils.update(menu,xmlPath);
                     if(result) {
                    	 List<Menu> menus   = (List<Menu>) Cache.getValue(Const.MENUS_KEY);
                    	 if(menus!=null) {
                    		 Iterator<Menu> itr = menus.iterator();
                        		while(itr.hasNext()){
                        		    if(itr.next().getId().equals(menu.getId())) {
                        		    	itr.remove();
                        		    }
                        		}
                        		menus.add(menu);
                    	 }
                     }
                     return result;
                 }
            }
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return false;
    }
    
    
    public Map<String,Object> addTopic(String historyId,String menuid, String title,String type, String content, String description,String viewcontent,String topicurl,String visible) {
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("CODE", "0");
    	try {
            
            String time = format.get().format(new Date());
           
            Topic topic = new Topic();
            String topicId =SnowflakeIdUtil.getSnowflakeId()+"";
            if(StringUtils.isNotBlank(historyId)) {
            	topicId = historyId;
            }
            String topicXmlPath = getXMLPath(Topic.class, topicId);
            topic.setId(topicId);
            topic.setTitle(title);
            topic.setDescription(description);
            topic.setVisible(visible);
            topic.setType(type);
            topic.setMenuid(menuid);
            topic.setCreate_datetime(time);
            topic.setUpdate_datetime(time);
            topic.setTopicurl(topicurl);
            
            String xmlMenuPath = this.getXMLPath(Menu.class, menuid);
        	Menu menu = XMLUtils.select(Menu.class,xmlMenuPath);
        	topic.setMenu_name(menu.getName());
        	String img = menu.getImgurl();
        	if(StringUtils.isBlank(img)) {
        		img="/image/txt.png";
        	}
        	topic.setMenu_img(img);
            Content content2 = new Content();
            String contentXmlPath = getXMLPath(Content.class, topicId);
            content2.setId(topicId);
            content2.setContent(content);
            content2.setViewcontent(viewcontent);
            
            Boolean flag1 = XMLUtils.save(topic, topicXmlPath);
            if(flag1) {
            	 List<Topic> topics   = (List<Topic>) Cache.getValue(Const.TOPICS_KEY);
            	 if(topics!=null) {
            		 //新增，这里就直接添加
            		 topics.add(topic);
            	 }
             }
            Boolean flag2 = XMLUtils.save(content2, contentXmlPath);
            Boolean flag = flag1&&flag2;
            if(flag) {
            	result.put("CODE", "0");
            	result.put("obj",topicId);
            }else {
            	result.put("CODE", "1");
            }
        }catch (Exception ex){
            log.error(ex.getMessage());
            result.put("CODE", "1");
        }
        return result;
    }
    
    public Map<String,Object>  updateTopic(String menuid, String topicid, String title,String type, String content, String description,String viewcontent,String topicurl,String visible) {
    	 Map<String,Object> result = new HashMap<String,Object>();
         result.put("CODE", "0");
    	try {
        	 if(StringUtils.isNotBlank(topicid)) {
        		 String time = format.get().format(new Date());
        		 String topicXmlPath = getXMLPath(Topic.class, topicid);
        		 String contentXmlPath = getXMLPath(Content.class, topicid);
        		 Topic topic = XMLUtils.select(Topic.class, topicXmlPath);
        		 Content content2 = XMLUtils.select(Content.class, contentXmlPath);
        		 if(topic!=null&&content2!=null) {
        	         topic.setTitle(title);
        	         topic.setDescription(description);
        	         topic.setVisible(visible);
        	         topic.setType(type);
        	         topic.setMenuid(menuid);
        	         topic.setUpdate_datetime(time);
        	         topic.setTopicurl(topicurl);
        	         String xmlMenuPath = this.getXMLPath(Menu.class, menuid);
        	         Menu menu = XMLUtils.select(Menu.class,xmlMenuPath);
        	         topic.setMenu_name(menu.getName());
        	         String img = menu.getImgurl();
        	        	if(StringUtils.isBlank(img)) {
        	        		img="/image/txt.png";
        	        	}
        	        	topic.setMenu_img(img);
        	         content2.setContent(content);
        	         content2.setViewcontent(viewcontent);
        	         Boolean flag1 = XMLUtils.update(topic, topicXmlPath);
        	         if(flag1) {
        	        	 List<Topic> topics   = (List<Topic>) Cache.getValue(Const.TOPICS_KEY);
                    	 if(topics!=null) {
                    		 Iterator<Topic> itr = topics.iterator();
                        		while(itr.hasNext()){
                        		    if(itr.next().getId().equals(topic.getId())) {
                        		    	itr.remove();
                        		    }
                        		}
                        		topics.add(topic);
                    	 }
                     }
        	         Boolean flag2 = XMLUtils.update(content2, contentXmlPath);
        	         
        	         //更新了的话直接移除
        	         Cache.remove("TOPIC_"+topicid);
        	         Cache.remove("CONTENT_"+topicid);
        	         
        	         Boolean flag = flag1&&flag2;
        	         if(flag) {
        	            result.put("CODE", "0");
        	            result.put("obj",topicid);
        	         }else {
        	            result.put("CODE", "1");
        	        }
        		 }
        	 }
        }catch (Exception ex){
            log.error(ex.getMessage());
            result.put("CODE", "1");
        }
        return result;
    }
    

    
    public Boolean deleteTopic(String topicid) {
    	try {
        	String xmlPath = this.getXMLPath(Topic.class, topicid);
            Boolean result = XMLUtils.delete(xmlPath);
            if(result) {
	        	 List<Topic> topics   = (List<Topic>) Cache.getValue(Const.TOPICS_KEY);
           	 if(topics!=null) {
           		 Iterator<Topic> itr = topics.iterator();
               		while(itr.hasNext()){
               		    if(itr.next().getId().equals(topicid)) {
               		    	itr.remove();
               		    }
               		}
           	 }
            }
            //更新了的话直接移除
	        Cache.remove("TOPIC_"+topicid);
	        Cache.remove("CONTENT_"+topicid);
            return result;
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return false;
    }


    public Boolean visibleTopic(String topicid, String visible) {
    	try {
        	String xmlPath = this.getXMLPath(Topic.class, topicid);
        	Topic topic = XMLUtils.select(Topic.class,xmlPath);
            if(topic!=null){
            	 if(!topic.getVisible().equals(visible)){
            		 topic.setVisible(visible);
                     Boolean result = XMLUtils.update(topic,xmlPath);
                     if(result) {
        	        	 List<Topic> topics   = (List<Topic>) Cache.getValue(Const.TOPICS_KEY);
                    	 if(topics!=null) {
                    		 Iterator<Topic> itr = topics.iterator();
                        		while(itr.hasNext()){
                        		    if(itr.next().getId().equals(topic.getId())) {
                        		    	itr.remove();
                        		    }
                        		}
                        		topics.add(topic);
                    	 }
                     }
                     //更新了的话直接移除
        	         Cache.remove("TOPIC_"+topicid);
        	         Cache.remove("CONTENT_"+topicid);
                     return result;
                 }
            }
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return false;
    }
    
   /**
    *  	这里只是简单的添加访问统计,只支持首页和博文
    * @param id
    * @return
    */
    public void addTopicVisit(String visitId,String type) {
        try {
        	 if(Const.TOPIC_VISIT.equals(type)) {
        		 //需要校验主题存不存在
        		 Topic topic = (Topic) Cache.getValue("TOPIC_"+visitId);
            	 if(topic==null) {
            		 String topicXmlPath = getXMLPath(Topic.class, visitId);
            		 topic = XMLUtils.select(Topic.class, topicXmlPath);
            	 }
        		 if(topic==null) {
        			 return;
        		 }
        		 //这里做页面的访问和主题的访问
        		 addVisit(visitId);
        		 addVisit(Const.PAGE_VISIT_ID);
        	 }
        	 //这里只做主题的访问
        	 if(Const.PAGE_VISIT.equals(type)) {
        		 addVisit(Const.PAGE_VISIT_ID);
        	 }
    		
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return;
    }
    /**
     * 	这里需要做同步操作，因为不是redis的自增，后续需要考虑单个visit做同步
     *	  反正访问量这么少，直接这里做同步先，后面再考虑别的
     * @param visitId
     */
    public synchronized void  addVisit(String visitId) {
    	try {
    	 String xmlPath = getXMLPath(Visit.class, visitId);
    	 //这里表示这个博文存在，可以添加访问记录，获取访问记录文件
		 Visit visit = (Visit) Cache.getValue("VISIT_"+visitId);
    	 if(visit==null) {
    		 visit = XMLUtils.select(Visit.class, xmlPath);
    		 if(visit!=null) {
    			 visit.setNum((Long.parseLong(visit.getNum())+1)+"");
    		 }else {
    			 visit = new Visit();
    			 visit.setNum(1+"");
    		 }
    	 }else {
    		 visit.setNum((Long.parseLong(visit.getNum())+1)+"");
    	 }
    	 //重新保存
    	 XMLUtils.save(visit, xmlPath);
    	 //放回缓存
    	 Cache.setValue("VISIT_"+visitId,visit);
    	}catch (Exception ex){
            log.error(ex.getMessage());
        }
        return;
    }
    
    //获取访问记录数
    public  String  getVisit(String visitId) {
	   	 String num = "0";
	 	try {
			 Visit visit = (Visit) Cache.getValue("VISIT_"+visitId);
		   	 if(visit==null) {
		   		 String xmlPath = getXMLPath(Visit.class, visitId);
		   		 visit = XMLUtils.select(Visit.class, xmlPath);
		   		 if(visit!=null) {
		   			 Cache.setValue("VISIT_"+visitId,visit);
		   		 }
		   	 }
		   	 if(visit!=null) {
		   		num=visit.getNum();
		   	 }
	   	 //这表明该页面以及访问了
	 	}catch (Exception ex){
	 		log.error(ex.getMessage());
	 	}
	 	return (Long.parseLong(num)+1)+"";
    }
    

    
    public Boolean addSetUp(String id, String blog_name, String copyright,
    		String beian_num,String phone, String name,String qq,String email,
    		String desc,String password,String code) {
        try {
              String xmlPath = this.getXMLPath(SetUp.class, id);
              //1、先获取旧的
              SetUp old = this.getSetUp(id);
              SetUp setUp = new SetUp(id, blog_name, copyright, beian_num, phone, name, qq, email, desc);
              String pd = old.getPassword();
              if(StringUtils.isNotBlank(password)) {
            	  pd=password;
              }
              if(StringUtils.isNotBlank(code)) {
            	  setUp.setCode(code);
              }
              setUp.setPassword(pd);
              setUp.setHash(old.getHash());
              setUp.setGy(old.getGy());
              Boolean result = XMLUtils.save(setUp, xmlPath);
              if(result) {
            	  Cache.setValue(Const.SETUP_KEY, setUp);
              }
              return result;
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return false;
    }
    
    /**
     * SetUp
     * @return
     */
    public SetUp getSetUp(String id){
    	SetUp setUp   = (SetUp) Cache.getValue(Const.SETUP_KEY);
	     if(setUp==null) {
	    	String xmlPath = getXMLPath(SetUp.class, id);
	    	setUp = XMLUtils.select(SetUp.class, xmlPath);
	    	if(setUp==null) {
	    		setUp  =new SetUp();
	    	}
	    	Cache.setValue(Const.SETUP_KEY,setUp);
	     }
	     return setUp;
	     
    }
    /**
     * 保存公钥，Hash和CODE
     * @param setUp
     */
	public void addSetUp(SetUp setUp) {
	    String xmlPath = this.getXMLPath(SetUp.class, setUp.getId());
        //1、先获取旧的
        SetUp old = this.getSetUp(setUp.getId());
        old.setHash(setUp.getHash());
        old.setGy(old.getGy());
        old.setCode(setUp.getCode());
        Boolean result = XMLUtils.save(old, xmlPath);
        if(result) {
      	  Cache.setValue(Const.SETUP_KEY, old);
        }
	}
	public int getTopicSize() {
		//获取主题数目
		List<Topic> topics   = getTopics();
		return topics.size();
		
	}
}
