package com.suibibk.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * 菜单
 * @author LWH
 *
 */
public class Menu  implements Comparable<Menu>{
	 private String id;//主键ID,雪花算法生成
	 private String name;//菜单名称
	 private String remark;//菜单备注
	 private String link;//菜单链接，只有外链有
	 private String imgurl;//菜单图片
	 private String sort;//菜单排序，从1开始
	 private String type;//菜单类别1是标签，2是外链，0是目录
	 private String visible;//是否有效0无效，1有效
	 private String parent_id;//上级ID
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVisible() {
		return visible;
	}
	public void setVisible(String visible) {
		this.visible = visible;
	}
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	@Override
	public String toString() {
		return "Menu [id=" + id + ", name=" + name + ", remark=" + remark + ", link=" + link + ", imgurl=" + imgurl
				+ ", sort=" + sort + ", type=" + type + ", visible=" + visible + ", parent_id=" + parent_id + "]";
	}
	@Override
	public int compareTo(Menu o) {
		int sort1 = Integer.parseInt(this.getSort());
		int sort2 = Integer.parseInt(o.getSort());
		return sort1-sort2;
	}
	

	
}
