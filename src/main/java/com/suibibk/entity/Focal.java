package com.suibibk.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * 首焦图
 * @author forever
 *
 */
public class Focal   implements Comparable<Focal>{
	private String id;//主键ID,雪花算法生成
	private String  focal_name;//首焦图名字
	private String focal_url;//首焦图路径
	private String focal_link;//跳转路径
	private String sort;//首焦图排序
	private String type;//01首焦图。02友情链接,03音乐
	private String visible;//是否可见
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFocal_name() {
		return focal_name;
	}
	public void setFocal_name(String focal_name) {
		this.focal_name = focal_name;
	}
	public String getFocal_url() {
		return focal_url;
	}
	public void setFocal_url(String focal_url) {
		this.focal_url = focal_url;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getVisible() {
		return visible;
	}
	public void setVisible(String visible) {
		this.visible = visible;
	}
	
	public String getFocal_link() {
		return focal_link;
	}
	public void setFocal_link(String focal_link) {
		this.focal_link = focal_link;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "Focal [id=" + id + ", focal_name=" + focal_name + ", focal_url=" + focal_url + ", focal_link="
				+ focal_link + ", sort=" + sort + ", type=" + type + ", visible=" + visible + "]";
	}
	@Override
	public int compareTo(Focal o) {
		int sort1 = Integer.parseInt(this.getSort());
		int sort2 = Integer.parseInt(o.getSort());
		return sort1-sort2;
	}
}
