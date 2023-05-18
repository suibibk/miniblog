package com.suibibk.entity;

public class Topic   implements Comparable<Topic>{
    private String id;//主键ID,雪花算法生成',
    private String title ;//'博文标题',
    private String description;//博文描述，截取博文的前100个字，去除掉各种信息',
    private String topicurl;
    private String visible;//是否有效0无效，1有效',
    private String menuid;//主题所在的标签',
    private String type;//类型1是markdown,2是普通编辑器
    private String create_datetime;//发布时间',
    private String update_datetime;//更新时间'
    
    private String menu_name;//菜单名称
    private String menu_img;//menu的头像
    
	public String getId() {
		return id;
	}
	public String getMenu_name() {
		return menu_name;
	}
	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTopicurl() {
		return topicurl;
	}
	public void setTopicurl(String topicurl) {
		this.topicurl = topicurl;
	}
	public String getVisible() {
		return visible;
	}
	public void setVisible(String visible) {
		this.visible = visible;
	}
	public String getMenuid() {
		return menuid;
	}
	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCreate_datetime() {
		return create_datetime;
	}
	public void setCreate_datetime(String create_datetime) {
		this.create_datetime = create_datetime;
	}
	public String getUpdate_datetime() {
		return update_datetime;
	}
	public void setUpdate_datetime(String update_datetime) {
		this.update_datetime = update_datetime;
	}
	
	
	public String getMenu_img() {
		return menu_img;
	}
	public void setMenu_img(String menu_img) {
		this.menu_img = menu_img;
	}
	
	
	@Override
	public String toString() {
		return "Topic [id=" + id + ", title=" + title + ", description=" + description + ", topicurl=" + topicurl
				+ ", visible=" + visible + ", menuid=" + menuid + ", type=" + type + ", create_datetime="
				+ create_datetime + ", update_datetime=" + update_datetime + ", menu_name=" + menu_name + ", menu_img="
				+ menu_img + "]";
	}
	@Override
	public int compareTo(Topic o) {
		Long sort1 = Long.parseLong(this.getId());
		Long sort2 = Long.parseLong(o.getId());
		//这里要反过来排序
		Long s = sort2-sort1;
		if(s>0) {
			return 1;
		}
		if(s<0) {
			return -1;
		}else {
			return 0;
		}
	}
}
