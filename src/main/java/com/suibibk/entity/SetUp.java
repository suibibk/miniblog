package com.suibibk.entity;


/**
 * 网站设置
 * @author forever
 *
 */
public class SetUp{
	private String id="1";//主键ID,雪花算法生成
	private String blog_name="迷你博客";//博客名字
	private String copyright="小林软件工作室";//首焦图路径
	private String beian_num="XXX备XXXX号";//备案号
	private String phone="XXXXXXXXXXX";//手机号
	private String name="迷你酱";//姓名
	private String qq="XXXXXXXXX";
	private String email="XXXXXX@XX.XXX";
	private String desc="迷你博客，记录生活";
	private String password = "7b3cb97050ead66122f4b9812904b55d1a50efbc90f10743d073c89c24dfbaed";
	
	private String hash="";//hash值
	private String code="";//校验码
	private String gy = "";//公钥
	
	public String getGy() {
		return gy;
	}
	public void setGy(String gy) {
		this.gy = gy;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public SetUp() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBlog_name() {
		return blog_name;
	}
	public void setBlog_name(String blog_name) {
		this.blog_name = blog_name;
	}
	public String getCopyright() {
		return copyright;
	}
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}
	public String getBeian_num() {
		return beian_num;
	}
	public void setBeian_num(String beian_num) {
		this.beian_num = beian_num;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "SetUp [id=" + id + ", blog_name=" + blog_name + ", copyright=" + copyright + ", beian_num=" + beian_num
				+ ", phone=" + phone + ", name=" + name + ", qq=" + qq + ", email=" + email + ", desc=" + desc
				+ ", password=" + password + ", hash=" + hash + ", code=" + code + ", gy=" + gy + "]";
	}
	public SetUp(String id, String blog_name, String copyright, String beian_num, String phone, String name, String qq,
			String email, String desc) {
		super();
		this.id = id;
		this.blog_name = blog_name;
		this.copyright = copyright;
		this.beian_num = beian_num;
		this.phone = phone;
		this.name = name;
		this.qq = qq;
		this.email = email;
		this.desc = desc;
	}
	
	
}
