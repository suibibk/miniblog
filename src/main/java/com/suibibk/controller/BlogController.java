package com.suibibk.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suibibk.annotation.LoginCheck;
import com.suibibk.entity.Content;
import com.suibibk.entity.Focal;
import com.suibibk.entity.Menu;
import com.suibibk.entity.SetUp;
import com.suibibk.entity.Topic;
import com.suibibk.service.CommonService;
import com.suibibk.utils.Const;
import com.suibibk.utils.HttpServletRequestUtil;

@Controller
public class BlogController {
	private static final Logger log = LoggerFactory.getLogger(BlogController.class);
	@Autowired
	private CommonService commonService;
	@Value("${upload.path}")
	private String uploadPath;
	
	@GetMapping("/")
	public String index(HttpServletRequest request) {
		try {
			Long start = System.currentTimeMillis();
			String page = request.getParameter("page");
			if(StringUtils.isBlank(page)) {
				page="1";
			}
			
			if(Integer.parseInt(page)<1) {
				throw new Exception("参数不正确");
			}
			request.setAttribute("page", page);
			//获得第一批
			Map<String,Object> result =commonService.getTopics("",Integer.parseInt(page),Const.NUM,"1");
			List<Topic> topics = (List<Topic>)result.get("TOPICS");
			String tip = page+"&nbsp;下一页&nbsp;"+(Integer.parseInt(page)+1);
			if(topics==null||topics.size()<=0) {
				//如果是最后一页，则重新跳回第一页
				if(!"1".equals(page)) {
					 return "redirect:/";
				}
				tip = "未查询到记录";
			}
			request.setAttribute("tip",tip);
			request.setAttribute("topics",topics);
			
			
			//---------------分页------------------//
			//总记录数
			int total = (int) result.get("TOTAL");
			//每页数目
			Double num =Double.valueOf(Const.NUM+"");
			//总共有多少页，向上取整
			int page_code = new Double(Math.ceil(total/num)).intValue();
			request.setAttribute("TOTAL",total);
			request.setAttribute("NUM", Const.NUM);
			request.setAttribute("PAGE_CODE", page_code);
			
			//-----------------分页----------------------//
			List<Focal> focals = commonService.getFocals("01","1");
			
			
			//公告
			List<Focal> ggs =  commonService.getFocals("04","1");
			
			//音乐
			/*List<Focal> musics = commonService.getFocals("03","1");
			request.setAttribute("musics",musics);*/
			
			//菜单
			List<Menu> menus  =commonService.getMenus("1");
			request.setAttribute("menus",menus);
			
			request.setAttribute("focals",focals);
			request.setAttribute("ggs",ggs);
			//获取首页访问数
			String PAGE_VISIT = commonService.getVisit(Const.PAGE_VISIT_ID);
			request.setAttribute("PAGE_VISIT",PAGE_VISIT);
			Long end = System.currentTimeMillis();
			System.out.println("index:耗时"+(end-start));
			
			SetUp setUp=commonService.getSetUp("1");
			request.setAttribute("setUp",setUp);
			return "blog/index";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "redirect:/";
		}
	}
	
	
	/*@GetMapping("/userHTML")
	public String userHTML(){
		return "userHTML";
	}
	@GetMapping("/indexHTML")
	public String indexHTML(){
		return "indexHTML";
	}
	@GetMapping("/blogHTML")
	public String blogHTML(){
		return "blogHTML";
	}
	@GetMapping("/topicHTML")
	public String topicHTML(){
		return "topicHTML";
	}*/
	/************************这个是到菜单的按钮，推荐用第二个******************************/
	
	/************************这个是到菜单的按钮，推荐用第二个******************************/
	@GetMapping("/menu/{menuid}")
	public String menu(@PathVariable("menuid") String menuid) {
		try {
			Long start = System.currentTimeMillis();
			HttpServletRequest request  = HttpServletRequestUtil.getRequest();
			//System.out.println("mgruserid:"+request.getSession().getAttribute("mgruserid"));
			if(request.getSession().getAttribute("mgruserid")==null){
				request.getSession().setAttribute("mgruserid","");
			}
			String page = request.getParameter("page");
			if(StringUtils.isBlank(page)) {
				page="1";
			}
			if(Integer.parseInt(page)<1) {
				throw new Exception("参数不正确");
			}
			request.setAttribute("page", page);
			Map<String,Object> result =commonService.getTopics(menuid,Integer.parseInt(page),Const.NUM,"1");
			List<Topic> topics = (List<Topic>)result.get("TOPICS");
			String tip = page+"&nbsp;下一页&nbsp;"+(Integer.parseInt(page)+1);
			if(topics==null||topics.size()<=0) {
				//如果是最后一页，则重新跳回第一页
				if(!"1".equals(page)) {
					 return "redirect:/menu/"+menuid;
				}else {
					//第一页都没有记录，跳回到首页
					return "redirect:/";
				}
			}
			request.setAttribute("tip",tip);
			request.setAttribute("menuid",menuid);
			request.setAttribute("topics",topics);
			SetUp setUp=commonService.getSetUp("1");
			request.setAttribute("setUp",setUp);
			
			//---------------分页------------------//
			//总记录数
			int total = (int) result.get("TOTAL");
			//每页数目
			Double num =Double.valueOf(Const.NUM+"");
			//总共有多少页，向上取整
			int page_code = new Double(Math.ceil(total/num)).intValue();
			request.setAttribute("TOTAL",total);
			request.setAttribute("NUM", Const.NUM);
			request.setAttribute("PAGE_CODE", page_code);
			//-----------------分页----------------------//
			//菜单
			List<Menu> menus  =commonService.getMenus("1");
			request.setAttribute("menus",menus);
			String menuImg = "/image/txt.png";
			for (Menu menu : menus) {
				if(menuid.equals(menu.getId())) {
					if(StringUtils.isNotBlank(menu.getImgurl())) {
						menuImg = menu.getImgurl();
					}
					request.setAttribute("menu",menu);
					break;
				}
			}
			
			//音乐
			/*List<Focal> musics = commonService.getFocals("03","1");
			request.setAttribute("musics",musics);*/
			request.setAttribute("menuImg",menuImg);
			//获取首页访问数
			String PAGE_VISIT = commonService.getVisit(Const.PAGE_VISIT_ID);
			request.setAttribute("PAGE_VISIT",PAGE_VISIT);
			Long end = System.currentTimeMillis();
			System.out.println("menu:耗时"+(end-start));
			return "blog/blog";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 return "redirect:/menu/"+menuid;
		}
	}
	/*******************博文详情访问，现在有两个方法，这个是第一个：现在推荐使用第二个，当然第一个也保留********************/

	/**
	 * 添加或者修改博页面，若是topicid=0,那么久是添加，否则就可能是修改
	 * @return topic页面
	 */
	@GetMapping("/topic/{topicid}")
	public String topic(@PathVariable("topicid") String topicid){
		Long start = System.currentTimeMillis();
		HttpServletRequest request  = HttpServletRequestUtil.getRequest();
		//页面不能用Long
		request.setAttribute("topicid",topicid);
		String title="";
		String pageview="";
		String desc="";
		String hot="";
		String replynum="";
		String create_datetime="";
		String content="";
		String filename="";
		String type="1";//1是markdown 2是普通编辑器
        String userid="";
        String menuid="";
        String u_name="";
        String u_imgurl="";
        String m_name="";
        String m_imgurl="";
        String topicurl="";
        //上一篇，下一篇
        String pre = "NO";
        String preId = "";
        String preTitle = "";
        String next = "NO";
        String nextId = "";
        String nextTitle = "";
        
        Map<String,Object> map = commonService.getTopic(topicid,"1");
		if(map!=null) {
			Topic topic = (Topic) map.get("TOPIC");
			Content content2 = (Content) map.get("CONTENT");
			if(topic!=null) {
				title=topic.getTitle();
	            create_datetime=topic.getCreate_datetime();
	            desc = topic.getDescription();
	            type=topic.getType();
	            menuid=topic.getMenuid()+"";
	            topicurl=topic.getTopicurl();
	            if(StringUtils.isBlank(topicurl)) {
	            	topicurl="/image/logo.png";
	            }
			}
			if(content2!=null) {
				content = content2.getViewcontent();
			}
			
			//查看是否有上一页下一页
			 Map<String,Object> preNextTopic = (Map<String, Object>) map.get("preNextTopic");
			 if(preNextTopic!=null) {
				 String pre1 = (String) preNextTopic.get("pre");
				 if("HAVE".equals(pre1)) {
					 Topic preTopic = (Topic) preNextTopic.get("preTopic");
					 if(preTopic!=null) {
						 pre = pre1;
						 preId =preTopic.getId();
						 preTitle = preTopic.getTitle();
					 }
				 }
				 String next1 = (String) preNextTopic.get("next");
				 if("HAVE".equals(next1)) {
					 Topic nextTopic = (Topic) preNextTopic.get("nextTopic");
					 if(nextTopic!=null) {
						 next = next1;
						 nextId =nextTopic.getId();
						 nextTitle = nextTopic.getTitle();
					 }
				 }
			 }
			
		}else {
			 return "redirect:/";
		}
		SetUp setUp=commonService.getSetUp("1");
		request.setAttribute("setUp",setUp);
        request.setAttribute("title",title);
		request.setAttribute("pageview",pageview);
		request.setAttribute("desc",desc);
		request.setAttribute("hot",hot);
		request.setAttribute("topicurl",topicurl);
		request.setAttribute("replynum",replynum);
		request.setAttribute("create_datetime",create_datetime);
		request.setAttribute("content",content);
		request.setAttribute("filename",filename);
		request.setAttribute("type",type);
		request.setAttribute("userid",userid);
		request.setAttribute("userid2",userid);
		request.setAttribute("menuid",menuid);
		request.setAttribute("menuid2",menuid);
		request.setAttribute("u_name",u_name);
		request.setAttribute("u_imgurl",u_imgurl);
		request.setAttribute("m_name",m_name);
		request.setAttribute("m_imgurl",m_imgurl);
		
		request.setAttribute("pre",pre);
		request.setAttribute("preId",preId);
		request.setAttribute("preTitle",preTitle);
		request.setAttribute("next",next);
		request.setAttribute("nextId",nextId);
		request.setAttribute("nextTitle",nextTitle);
		 
		
		//菜单
		List<Menu> menus = commonService.getMenus("1");
		request.setAttribute("menus",menus);
		
		for (Menu menu : menus) {
			if(menuid.equals(menu.getId())) {
				request.setAttribute("menu",menu);
				break;
			}
		}
		
		//音乐
		/*List<Focal> musics = commonService.getFocals("03","1");
		request.setAttribute("musics",musics);*/
		//获取首页访问数
		String PAGE_VISIT = commonService.getVisit(Const.PAGE_VISIT_ID);
		request.setAttribute("PAGE_VISIT",PAGE_VISIT);
		String TOPIC_VISIT = commonService.getVisit(topicid);
		request.setAttribute("TOPIC_VISIT",TOPIC_VISIT);
		Long end = System.currentTimeMillis();
		System.out.println("topic:耗时"+(end-start));
		return "blog/topic";
	}
	
	
	

	/**********************博文访问*********************************/
	
	@GetMapping("/search")
	public String search(HttpServletRequest request) {
		Long start = System.currentTimeMillis();
		String value = request.getParameter("value");
		try {
			String page = request.getParameter("page");
			if(StringUtils.isBlank(page)) {
				page="1";
			}
			
			if(Integer.parseInt(page)<1) {
				throw new Exception("参数不正确");
			}
			request.setAttribute("page", page);
			//获得第一批
			Map<String,Object> result  =commonService.searchTopics("",Integer.parseInt(page),Const.NUM,"1",value);
			List<Topic> topics = (List<Topic>)result.get("TOPICS");
			String tip = page+"&nbsp;下一页&nbsp;"+(Integer.parseInt(page)+1);
			if(topics==null||topics.size()<=0) {
				//如果是最后一页，则重新跳回第一页
				if(!"1".equals(page)) {
					 return "redirect:/";
				}
				tip = "未查询到记录";
			}
			request.setAttribute("tip",tip);
			request.setAttribute("topics",topics);
			//菜单
			List<Menu> menus  =commonService.getMenus("1");
			request.setAttribute("menus",menus);
			
			//---------------分页------------------//
			//总记录数
			int total = (int) result.get("TOTAL");
			//每页数目
			Double num =Double.valueOf(Const.NUM+"");
			//总共有多少页，向上取整
			int page_code = new Double(Math.ceil(total/num)).intValue();
			request.setAttribute("TOTAL",total);
			request.setAttribute("NUM", Const.NUM);
			request.setAttribute("PAGE_CODE", page_code);
			//音乐
			/*List<Focal> musics = commonService.getFocals("03","1");
			request.setAttribute("musics",musics);*/
			request.setAttribute("value",value);
			//获取首页访问数
			String PAGE_VISIT = commonService.getVisit(Const.PAGE_VISIT_ID);
			request.setAttribute("PAGE_VISIT",PAGE_VISIT);
			Long end = System.currentTimeMillis();
			System.out.println("search:耗时"+(end-start));
			SetUp setUp=commonService.getSetUp("1");
			request.setAttribute("setUp",setUp);
			return "blog/search";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "redirect:/";
		}
	}
	
	/**
	 * 添加或者修改博页面，若是topicid=0,那么久是添加，否则就可能是修改
	 * @return topic页面
	 */
	@GetMapping("/music")
	public String music(){
		HttpServletRequest request  = HttpServletRequestUtil.getRequest();
		//菜单
		/*List<Menu> menus = commonService.getMenus("1");
		request.setAttribute("menus",menus);*/
		
		//音乐
		List<Focal> musics = commonService.getFocals("03","1");
		request.setAttribute("musics",musics);
		
		return "blog/music";
	}

	/**
	 * 获取资源文件，不需要经过nginx,这样子，这里就可以控制某些资源是否可以访问，某些资源不可以访问
	 * @param path1
	 * @param path2
	 * @param path3
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/fileupload/{path1}/{path2}/{path3}",produces = MediaType.ALL_VALUE)
	@ResponseBody
	public byte[] getFile(@PathVariable("path1") String path1,@PathVariable("path2") String path2,@PathVariable("path3") String path3){
		if(!(uploadPath.contains("/")||uploadPath.contains("\\"))) {
			uploadPath=System.getProperty("user.dir")+File.separator+uploadPath;
		}
		String path = uploadPath+"/fileupload/"+path1+"/"+path2+"/"+path3;
		//log.info("path:"+path);
		File file = new File(path);
		byte[] bytes = null;
		try {
			FileInputStream inputStream = new FileInputStream(file);
			bytes = new byte[inputStream.available()];
			inputStream.read(bytes, 0, inputStream.available());
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bytes;
	}
  	@GetMapping("/logout")
  	public String logout(HttpServletRequest request){
		String link= request.getParameter("link");
  		HttpSession session = request.getSession();
  		String userid = (String) session.getAttribute("mgruserid");
  		if(StringUtils.isNotBlank(userid)) {
  			session.removeAttribute("mgruserid");
  		}
  		return "redirect:"+link;
  	}
  	
	  /**
		 * 修改状态
		 * @return
		 */
		@PostMapping("/addVisit")
		@ResponseBody
		public  Map<String,Object>  visibleFocal(HttpServletRequest request ) {
			String id= request.getParameter("id");
			String type= request.getParameter("type");
			commonService.addTopicVisit(id, type);
			Map<String,Object> result = new HashMap<String, Object>();
			result.put("CODE", "0");
			return result;
		}
  	
		/**
		 * 图形验证码
		 * @return
		 * @throws IOException
		 */
		@RequestMapping(value = "/getImage",produces = MediaType.ALL_VALUE)
	    @ResponseBody
	    public byte[] getImage() throws IOException {
	        SecureRandom random = new SecureRandom();
	        //图形验证码的宽度
	        int width=75;
	        //图形验证码的高度
	        int height = 30;
	        //图形验证码的颜色
	        Color backColor = new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255));
	        //图形验证码的字体颜色，跟背景颜色反色
	        Color fontColor = new Color(255-backColor.getRed(),255-backColor.getGreen(),255-backColor.getBlue());
	        //生成BufferedImage对象
	        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	        //得到画布
	        Graphics2D g = bi.createGraphics();
	        //获取字体:Sans Serif字体比较难以阅读，增加辨识难度
	        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 20);
	        //设置字体
	        g.setFont(font);
	        //设置画笔颜色
	        g.setColor(backColor);
	        //画背景
	        g.fillRect(0, 0, width, height);
	        //设置画笔颜色
	        g.setColor(fontColor);
	        //获取要画的验证码
	        String seq = "23456789abcdefghijkmnpqrstuvwxyzABCDEFGHIJKMNPQRSTUVWXYZ";
	        StringBuffer sb = new StringBuffer();
	        int j = 0;
	        for(int i=0;i<4;i++) {
	            j=random.nextInt(seq.length());
	            sb.append(seq.substring(j, j+1));
	        }
	        String code = sb.toString();
	        //画字符:画布宽度75，高度30，文字大小20，四个文字长度就是
	        //计算文字长度
	        FontMetrics fm = g.getFontMetrics(font);
	        int textWidth = fm.stringWidth(code);
	        int w = (width-textWidth)/2;
	        g.drawString(code, w, 22);
	        //画噪点:40个
	        for(int i=0;i<40;i++) {
	            g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
	            g.fillRect(random.nextInt(width), random.nextInt(height), 1, 1);
	        }
	        //返回图片流
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        try {
	        	ImageIO.write(bi, "jpg", out);
	        	} catch (IOException e) {
	        	e.printStackTrace();
	        	}
	        HttpServletRequest request  = HttpServletRequestUtil.getRequest();
	        request.getSession().setAttribute(Const.IMAGE_CODE_KEY, code);
	        return out.toByteArray();
	    }
		
  	public static void main(String[] args) {
	}
}
