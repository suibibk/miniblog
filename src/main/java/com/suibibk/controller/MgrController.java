package com.suibibk.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.suibibk.annotation.LoginCheck;
import com.suibibk.entity.Content;
import com.suibibk.entity.Focal;
import com.suibibk.entity.Menu;
import com.suibibk.entity.SetUp;
import com.suibibk.entity.Topic;
import com.suibibk.service.CommonService;
import com.suibibk.utils.CashCode;
import com.suibibk.utils.CommonFunction;
import com.suibibk.utils.Const;
import com.suibibk.utils.Encrypt;
import com.suibibk.utils.Html2Text;
import com.suibibk.utils.HttpUtils;
import com.suibibk.utils.RSA;

@Controller
@RequestMapping("/mgr")
public class MgrController {
	//默认加载的文章数
	private static final Logger log = LoggerFactory.getLogger(MgrController.class);
	private static final ThreadLocal<SimpleDateFormat> format = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyyMM");
		};
	};
	@Value("${upload.path}")
	private String uploadPath;
	
	@Autowired
	private CommonService commonService;
	/*
	 * 系统没有访问权限跳转的页面
	 */
	@RequestMapping("/auth")
	public String auth(HttpServletRequest request){return "mgr/auth";}
	
	@LoginCheck
	@GetMapping("")
	public String mgr(HttpServletRequest request) {
		return "mgr/mgr";
	}
	@LoginCheck
	//会重新跳转到这
	@GetMapping("/mobileTopic/{topicid}/{menuid}")
	public String mobileTopic(@PathVariable("topicid") String topicid,@PathVariable("menuid") String menuid) {
		//初始化权限菜单
		String title = "";
		String topicurl = "";
		String content = "";
		//若是topicid存在，则表明是修改，否则就是添加
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		if(!"0".equals(topicid)){
			Map<String,Object> map = commonService.getTopic(topicid,"");
			if(map!=null) {
				Topic topic = (Topic) map.get("TOPIC");
				Content content2 = (Content) map.get("CONTENT");
				if(topic!=null) {
					title = (String)topic.getTitle();
					menuid = topic.getMenuid()+"";
					topicurl = topic.getTopicurl();
				}
				if(content2!=null) {
					content =  (String)content2.getContent();
				}
			}
		}
		request.setAttribute("topicid",topicid+"");
		request.setAttribute("title",title+"");
		request.setAttribute("topicurl",topicurl+"");
		request.setAttribute("content",content+"");
		request.setAttribute("menuid",menuid+"");
		
		HttpSession session  =request.getSession();
		String userid = (String)session.getAttribute("mgruserid");
		//获取剩余次数
		request.setAttribute("num","1");
		request.setAttribute("myblog","/blog/"+userid);
		return "mgr/mobileTopic";
	}



	
	@LoginCheck
	@GetMapping("/top")
	public String top() {
		return "mgr/top";
	}
	@LoginCheck
	@GetMapping("/left")
	public String left() {
		return "mgr/left";
	}
	@LoginCheck
	@GetMapping("/menus")
	public String menus() {return "mgr/menus";}
	
	@LoginCheck
	@GetMapping("/focals")
	public String focal() {return "mgr/focals";}
	
	@LoginCheck
	@GetMapping("/types")
	public String types() {return "mgr/types";}
	
	@LoginCheck
	@GetMapping("/topics")
	public String topics() {return "mgr/topics";}
	/**
	 * 添加或者修改博页面，若是topicid=0,那么久是添加，否则就可能是修改
	 * @return topic页面
	 */
	
	@LoginCheck
	@GetMapping("/topic/{topicid}/{menuid}")
	public String topic(@PathVariable("topicid") String topicid,@PathVariable("menuid") String menuid){
		String title = "";
		String content = "";
		String topicurl="";
		//若是topicid存在，则表明是修改，否则就是添加
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		//这里就表明用户是修改，这里直接去获取用户的topic信息
		if(!"0".equals(topicid)){
			Map<String,Object> map = commonService.getTopic(topicid,"");
			if(map!=null) {
				Topic topic = (Topic) map.get("TOPIC");
				Content content2 = (Content) map.get("CONTENT");
				if(topic!=null) {
					title = (String)topic.getTitle();
					menuid = topic.getMenuid()+"";
					topicurl = topic.getTopicurl();
				}
				if(content2!=null) {
					content =  (String)content2.getContent();
				}
			}
		}
		request.setAttribute("topicid",topicid+"");
		request.setAttribute("topicurl",topicurl+"");
		request.setAttribute("title",title+"");
		request.setAttribute("content",content+"");
		request.setAttribute("menuid",menuid+"");
		//获取剩余次数
		request.setAttribute("num", "1");
		return "mgr/topic";
	}
	/**
	 * 添加菜单，过滤器已经做了空值检查，理论上应该做分页查询的，但是这里直接全部查询回来
	 * @return
	 */
	@LoginCheck
	@PostMapping("/getMenus")
	@ResponseBody
	public Map<String,Object> getMenus(HttpServletRequest request) {
		List<Menu> menus =commonService.getMenus("");
		log.debug("查询菜单："+menus);
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("CODE", "0");
		result.put("MENUS", menus);
		return result;
	}


	/**
	 * 添加菜单，过滤器已经做了空值检查
	 * @return
	 */
	@LoginCheck
	@PostMapping("/addMenus")
	@ResponseBody
	public Map<String,Object> addMenus(HttpServletRequest request ) {
		String menuid = request.getParameter("menuid");
		String type= request.getParameter("type");
		String name= request.getParameter("name");
		String remark= request.getParameter("remark");
		String imgurl= request.getParameter("imgurl");
		String link= request.getParameter("link");
		String sort= request.getParameter("sort");
		String parent_id= request.getParameter("parent_id");
		
		//做过滤
		menuid = Html2Text.getContent(menuid);
		type = Html2Text.getContent(type);
		name = Html2Text.getContent(name);
		remark = Html2Text.getContent(remark);
		imgurl = Html2Text.getContent(imgurl);
		link = Html2Text.getContent(link);
		sort = Html2Text.getContent(sort);
		parent_id = Html2Text.getContent(parent_id);
	    if(StringUtils.isNotBlank(menuid)) {
	    	commonService.updateMenu(menuid, type, name, remark, imgurl, link, sort, parent_id);
	    }else {
	    	commonService.addMenu(type, name, remark, imgurl, link, sort, parent_id);
	    }
	    Map<String,Object> result = new HashMap<String, Object>();
		result.put("CODE", "0");
		return result;
	}

	/**
	 * 删除菜单
	 * @return
	 */
	@LoginCheck
	@PostMapping("/deleteMenu")
	@ResponseBody
	public Map<String,Object>  deleteMenu(HttpServletRequest request ) {
		String menuid= request.getParameter("menuid");
		commonService.deleteMenu(menuid);
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("CODE", "0");
		return result;
	}
	/**
	 * 修改菜单状态
	 * @return
	 */
	@LoginCheck
	@PostMapping("/visibleMenu")
	@ResponseBody
	public  Map<String,Object>  visibleMenu(HttpServletRequest request ) {
		String menuid= request.getParameter("menuid");
		String visible= request.getParameter("visible");
		menuid = Html2Text.getContent(menuid);
		visible = Html2Text.getContent(visible);
		commonService.visibleMenu(menuid,visible);
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("CODE", "0");
		return result;
	}



	@LoginCheck
	@PostMapping("/getTopics")
	@ResponseBody
	public Map<String,Object>  getTopics(HttpServletRequest request) {
		String menuid= request.getParameter("menuid");
		String page= request.getParameter("page");
		if("".equals(page)||null==page){
			page="1";
		}
		Map<String,Object> map =commonService.getTopics(menuid,Integer.parseInt(page),Const.NUM,"");
		map.put("CODE", "0");
		return map;
	}
	@LoginCheck
	@PostMapping("/deleteTopic")
	@ResponseBody
	public Map<String,Object>   deleteTopic(HttpServletRequest request ) {
		String topicid= request.getParameter("topicid");
		commonService.deleteTopic(topicid);
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("CODE", "0");
		return result;
	}

	@LoginCheck
	@PostMapping("/saveTopic")
	@ResponseBody
	public Map<String,Object> saveTopic(HttpServletRequest request) {
		String menuid = request.getParameter("menuid");
		String title = request.getParameter("title");
		String type = request.getParameter("type");
		String topicid = request.getParameter("topicid");
		String content = request.getParameter("content");
		String viewcontent = request.getParameter("viewcontent");
		String description = request.getParameter("description");
		String topicurl = request.getParameter("topicurl");
		String visible = request.getParameter("visible");
		String historyId = request.getParameter("historyId");
		
		menuid = Html2Text.getContent(menuid);
		type = Html2Text.getContent(type);
		topicid = Html2Text.getContent(topicid);
		topicurl = Html2Text.getContent(topicurl);
		visible = Html2Text.getContent(visible);

		
		if(StringUtils.isBlank(visible)) {
			visible="1";
		}
		
		//这里开始去获取博文内容，只获取文字
		//log.info("description1:"+description);
		description = Html2Text.getContent(description);
		//log.info("description2:"+description);
		if(StringUtils.isBlank(description)){
		    //没有就直接放标题
			description=title;
		}else{
		    //实实在在的中文
		    if(description.length()>200){
		        description=description.substring(0,197)+"...";
            }
        }
		log.debug("description2:"+description);
		title=CommonFunction.zhuanyi(title);
		Map<String,Object> result = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(topicid)) {
			result = commonService.updateTopic(menuid,topicid,title,type,content,description,viewcontent,topicurl,visible);

		}else {
			//检查code是否正确
			Boolean check = checkCode();
			if(!check) {
				//校验不成功，检查有没有发上10篇文章
				int size = commonService.getTopicSize();
				if(size>10) {
					result.put("CODE", "2");
					return result;
				}
			}
			
			
			result = commonService.addTopic(historyId,menuid,title,type,content,description,viewcontent,topicurl,visible);
		}
		return result;
	}
	
	
	private Boolean checkCode() {
		SetUp setUp = commonService.getSetUp("1");
		try {
			if(setUp!=null) {
				//获取code
				String code = setUp.getCode();
				if(StringUtils.isNotBlank(code)) {
					//用公钥解密
					if(StringUtils.isNotBlank(setUp.getGy())) {
						String a = RSA.rsaDecode(setUp.getGy(), code);
						String p = Encrypt.encrypt(a);
						if(setUp.getHash().equals(p)) {
							return true;
						}
					}
					
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 修改博文状态
	 * @return
	 */
	@LoginCheck
	@PostMapping("/visibleTopic")
	@ResponseBody
	public Map<String,Object> visibleTopic(HttpServletRequest request ) {
		String topicid= request.getParameter("topicid");
		String visible= request.getParameter("visible");
		commonService.visibleTopic(topicid,visible);
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("CODE", "0");
		return result;
	}
	@LoginCheck
	@PostMapping("/uploadImg")
	@ResponseBody
	public Map<String,Object> uploadImg(@RequestParam(value = "editormd-image-file", required = true) MultipartFile file) throws IOException {
		String trueFileName = file.getOriginalFilename();
		String suffix = trueFileName.substring(trueFileName.lastIndexOf("."));
		if(!CommonFunction.checkImage(suffix.substring(1))){
			Map<String,Object> map =new HashMap<String,Object>();
			map.put("success", 0);
			map.put("message", "上传失败，格式不对");
			map.put("url", "");
			return map;
		}
		long  size= file.getSize();
		if(size>204800*5*10){
			//大小为最大200k，图片
			Map<String,Object> map =new HashMap<String,Object>();
			map.put("success", 0);
			map.put("message", "图片太大，最大不超过10M");
			map.put("url", "");
			return map;
		}
		log.debug("size:"+size);
		String fileName = System.currentTimeMillis()+suffix;
		String date =format.get().format(new Date());
		String type = "images";
		if(".mp3".equals(suffix.toLowerCase())){
			type="musics";
		}
		if(!(uploadPath.contains("/")||uploadPath.contains("\\"))) {
			uploadPath=System.getProperty("user.dir")+File.separator+uploadPath;
		}
		String path = uploadPath+"/fileupload/"+type+"/"+date+"/";
		//System.out.println(path);
		//System.out.println(fileName);
		File targetFile = new File(path, fileName);
		//获取父目录
		File fileParent = targetFile.getParentFile();
		//判断是否存在
		if (!fileParent.exists()) {
			fileParent.mkdirs();
		}
		if (!targetFile.exists()) {
			targetFile.createNewFile();
		}
		//保存
		try {
			file.transferTo(targetFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String filename = "/fileupload/"+type+"/"+date+"/"+fileName;
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("success", 1);
		map.put("message", "上传成功");
		map.put("url", filename);
		return map;
	}


	/*****************xheditor上传专用******************/
	@LoginCheck
	@PostMapping("/uploadImg2")
	@ResponseBody
	public Map<String,Object> uploadImg2(@RequestParam(value = "filedata", required = true) MultipartFile file) throws IOException {
		String trueFileName = file.getOriginalFilename();
		String suffix = trueFileName.substring(trueFileName.lastIndexOf("."));
		if(!CommonFunction.checkImage(suffix.substring(1))){
			Map<String,Object> map =new HashMap<String,Object>();
			map.put("err", "上传失败，格式不对");
			map.put("msg", "上传失败，格式不对");
			return map;
		}
		Long size = file.getSize();
		if(size>204800*5*10){
			//大小为最大200k，图片
			Map<String,Object> map =new HashMap<String,Object>();
			map.put("success", 0);
			map.put("message", "图片太大，最大不超过10M");
			map.put("url", "");
			return map;
		}
		String fileName = System.currentTimeMillis()+suffix;
		String date =format.get().format(new Date());
		if(!(uploadPath.contains("/")||uploadPath.contains("\\"))) {
			uploadPath=System.getProperty("user.dir")+File.separator+uploadPath;
		}
		String path = uploadPath+"/fileupload/images/"+date+"/";
		//System.out.println(path);
		//System.out.println(fileName);
		File targetFile = new File(path, fileName);
		//获取父目录
		File fileParent = targetFile.getParentFile();
		//判断是否存在
		if (!fileParent.exists()) {
			fileParent.mkdirs();
		}
		if (!targetFile.exists()) {
			targetFile.createNewFile();
		}
		//保存
		try {
			file.transferTo(targetFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String filename = "/fileupload/images/"+date+"/"+fileName;
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("err", "");
		map.put("msg", filename);
		return map;
	}
	
	/**
	 * 添加首焦图
	 * @return
	 */
	@LoginCheck
	@PostMapping("/addFocal")
	@ResponseBody
	public  Map<String,Object>  addFocal(HttpServletRequest request ) {
		String focal_name= request.getParameter("focal_name");
		String focal_url= request.getParameter("focal_url");
		String focal_link= request.getParameter("focal_link");
		String sort= request.getParameter("sort");
		String type= request.getParameter("type");
		commonService.addFocal(focal_name,focal_url,focal_link,sort,type);
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("CODE", "0");
		return result;
	}
	/**
	 * 修改首焦图
	 * @return
	 */
	@LoginCheck
	@PostMapping("/updateFocal")
	@ResponseBody
	public  Map<String,Object>  updateFocal(HttpServletRequest request ) {
		String id= request.getParameter("id");
		String focal_name= request.getParameter("focal_name");
		String focal_url= request.getParameter("focal_url");
		String focal_link= request.getParameter("focal_link");
		String sort= request.getParameter("sort");
		String type= request.getParameter("type");
		commonService.updateFocal(id,focal_name,focal_url,focal_link,sort,type);
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("CODE", "0");
		return result;
	}
	@LoginCheck
	@PostMapping("/getFocals")
	@ResponseBody
	public  Map<String,Object>  getFocals(HttpServletRequest request) {
		String type = request.getParameter("type");
		List<Focal> focals =commonService.getFocals(type,"");
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("CODE", "0");
		result.put("FOCALS", focals);
		return result;
	}
	/**
	 * 修改状态
	 * @return
	 */
	@LoginCheck
	@PostMapping("/visibleFocal")
	@ResponseBody
	public  Map<String,Object>  visibleFocal(HttpServletRequest request ) {
		String id= request.getParameter("id");
		String visible= request.getParameter("visible");
		commonService.visibleFocal(id,visible);
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("CODE", "0");
		return result;
	}
	@LoginCheck
	@PostMapping("/deleteFocal")
	@ResponseBody
	public  Map<String,Object>  deleteFocal(HttpServletRequest request ) {
		String id= request.getParameter("id");
		commonService.deleteFocal(id);
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("CODE", "0");
		return result;
	}

	@LoginCheck
	@GetMapping("/setUp")
	public String setUp(HttpServletRequest request) {
		SetUp setUp=commonService.getSetUp("1");
		request.setAttribute("setUp",setUp);
		return "mgr/setUp";}

	
	@LoginCheck
	@PostMapping("/saveSetUp")
	@ResponseBody
	public  Map<String,Object>  saveSetUp(HttpServletRequest request ) {
		Map<String,Object> result = new HashMap<String, Object>();
		String blog_name= request.getParameter("blog_name");
		String copyright= request.getParameter("copyright");
		String beian_num= request.getParameter("beian_num");
		String phone= request.getParameter("phone");
		String name= request.getParameter("name");
		String qq= request.getParameter("qq");
		String email= request.getParameter("email");
		String desc= request.getParameter("desc");
		String code= request.getParameter("code");
		String password1= request.getParameter("password1");
		String password2= request.getParameter("password2");
		if(StringUtils.isNotBlank(password1)||StringUtils.isNotBlank(password2)) {
			if(StringUtils.isNotBlank(password1)) {
				if(!password1.equals(password2)) {
					result.put("CODE", "1");
					result.put("info", "两次密码不一致");
					return result;
				}
			}
			if(StringUtils.isNotBlank(password2)) {
				if(!password2.equals(password1)) {
					result.put("CODE", "1");
					result.put("info", "两次密码不一致");
					return result;
				}
			}
			password2 = Encrypt.encrypt(password2);
		}
		commonService.addSetUp("1", blog_name, copyright, beian_num, phone, name, qq, email, desc,password2,code);
		
		result.put("CODE", "0");
		result.put("info", "修改成功");
		return result;
	}
	
	@GetMapping("/login")
	public String login(HttpServletRequest request) {
		 String  mgruserid = (String)request.getSession().getAttribute("mgruserid");
		 if(StringUtils.isNotBlank(mgruserid)) {
			 return "redirect:/mgr";
		 }
		return "mgr/login";}
	
	@PostMapping("/toLogin")
	@ResponseBody
	public  Map<String,Object>  toLogin(HttpServletRequest request ) {
		Map<String,Object> result = new HashMap<String, Object>();
		String password= request.getParameter("password");
		String check_code= request.getParameter("check_code");
		if(StringUtils.isBlank(password)) {
			result.put("CODE", "1");
			result.put("info", "密码不能为空");
			return result;
		}
		if(StringUtils.isBlank(check_code)) {
			result.put("CODE", "2");
			result.put("info", "验证码不能为空");
			return result;
		}
		//检查验证码
		String code = (String) request.getSession().getAttribute(Const.IMAGE_CODE_KEY);
		if(StringUtils.isBlank(code)) {
			result.put("CODE", "2");
			result.put("info", "验证码不正确");
			request.getSession().removeAttribute(Const.IMAGE_CODE_KEY);
			return result;
		}
		if(!check_code.toLowerCase().equals(code.toLowerCase())) {
			result.put("CODE", "2");
			result.put("info", "验证码不正确");
			request.getSession().removeAttribute(Const.IMAGE_CODE_KEY);
			return result;
		}
		System.out.println(password);
		String p = Encrypt.encrypt(password);
		SetUp setUp = commonService.getSetUp("1");
		if(setUp!=null) {
			if(p.equals(setUp.getPassword())) {
				request.getSession().setAttribute("mgruserid",Const.LOGIN); 
				result.put("CODE", "0");
				result.put("info", "登录成功");
				return result;
			}
		}
		System.out.println(p);
		result.put("CODE", "1");
		result.put("info", "密码不正确");
		return result;
	}
	
	public static void main(String[] args) {
	}
	
}
