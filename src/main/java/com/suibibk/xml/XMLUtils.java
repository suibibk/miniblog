package com.suibibk.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * XML操作工具类，保存的时候进行类编码，查询的时候已经进行解码
 * @author forever
 *
 */
public class XMLUtils {
	/**
	 * 添加xml节点
	 * @param parent       父节点
	 * @param elementName  节点名称
	 * @param elementValue 节点值
	 * @return 返回新添加的节点
	 */
	private static Element addElement(Element parent, String elementName, String elementValue) {
		Element element = parent.addElement(elementName);
		if (!"".equals(elementValue)) {
			element.setText(elementValue);
		}
		return element;
	}

	// 写入XML文件
	/**
	 * Dom4j通过XMLWriter将Document对象表示的XML树写入指定的文件，并使用OutputFormat格式对象指定写入的风格和编码方法。
	 * 调用OutputFormat.createPrettyPrint()方法可以获得一个默认的pretty print风格的格式对象。
	 * 对OutputFormat对象调用setEncoding()方法可以指定XML文件的编码方法。
	 * @param document
	 * @param filename
	 * @return
	 */
	private static boolean saveXmlFile(Document document, String xmlPath) {
		boolean flag = true;
		try {
			// OutputFormat.createPrettyPrint() ： 表示格式化（就是有缩进的格式）
			//但是这种格式化会导致换行不见了
			OutputFormat format = new OutputFormat();
			XMLWriter writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(xmlPath), "UTF-8"), format);
			writer.setEscapeText(false);
			writer.write(document);
			writer.close();
		} catch (Exception ex) {
			flag = false;
			//ex.printStackTrace();
		}
		return flag;
	}


	// 读取XML文档
	private static  Document load(String xmlPath) {
		Document document = null;
		try {
			SAXReader saxReader = new SAXReader();
			document = saxReader.read(new File(xmlPath)); // 读取XML文件,获得document对象
		} catch (Exception ex) {
			//ex.printStackTrace();
		}
		return document;
	}
	/**
	 * 保存，这里需要指定保存的对象和文件所在路径
	 * @param object
	 * @param xmlPath
	 */
	public static Boolean save(Object object,String xmlPath) {
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.getDocumentFactory().createDocument();
			Class<?> clazz = object.getClass();
			String rootName = clazz.getSimpleName().toLowerCase();
			Element root = document.addElement(rootName+"s");
			Element content = addElement(root, rootName, "");
			for (Field declaredField : clazz.getDeclaredFields()) {
				declaredField.setAccessible(true);
				//获取属性名字
				String name = declaredField.getName();
				//获取属性值
				String value = (String) declaredField.get(object);
				if(value==null) {
					value="";
				}
				addElement(content, name,encode(value));
			}
			File file = new File(xmlPath);
			file.createNewFile();
			return saveXmlFile(document, xmlPath);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 删除xml
	 * @param xmlPath xml路径
	 */
	public static Boolean delete(String xmlPath) {
		try {
			File file = new File(xmlPath);
			file.delete();
			return true;
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return false;
	}
	/**
	 * xml更新 其实内部只有一条记录
	 * @param object 要更新的对象
	 * @param xmlPath 路径
	 * @return
	 */
	public static boolean update(Object object, String xmlPath) {
		try {
			Class<?> clazz = object.getClass();
			Document document = load(xmlPath);
			if (document == null) {
				return false;
			}
			Element root = document.getRootElement();
			List<Element> list = root.elements();
			for (Element p : list) {
				// 获取每个p元素中的name元素
				// 这里就找到了要修改的元素
				for (Field declaredField : clazz.getDeclaredFields()) {
					declaredField.setAccessible(true);
					String name = declaredField.getName();
					String value = (String) declaredField.get(object);
					if (value == null) {
						value = "";
					}
					List<Element> childs = p.elements();
					for (Element p2 : childs) {
						if (p2.getName().equals(name)) {
							p2.setText(encode(value));
							break;
						}
					}
				}
			}
			return saveXmlFile(document, xmlPath);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 查询内容
	 * @param clazz
	 * @param xmlPath
	 * @return
	 */
	public static <T> T select(Class<T> clazz, String xmlPath) {
		try {
			Document document = load(xmlPath);
			if (document == null) {
				return null;
			}
			Element root = document.getRootElement();
			T obj = clazz.newInstance();
			List<Element> list = root.elements();
			// 遍历p元素
			for (Element p : list) {
				// 获取每个p元素中的name元素
				for (Field declaredField : clazz.getDeclaredFields()) {
					declaredField.setAccessible(true);
					String name = declaredField.getName();
					List<Element> childs = p.elements();
					for (Element p2 : childs) {
						if (p2.getName().equals(name)) {
							Class<?> z = declaredField.getType();
							String value = (String) p2.getData();
							if(value==null) {
								value="";
							}
							declaredField.set(obj,decode(value));
							break;
						}
					}
				}

			}
			return obj;
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return null;
	}
	public static String encode(String str) {
		String content ="";
		if(StringUtils.isNotBlank(str)) {
			try {
				content ="<![CDATA["+str+"]]>";
				//content = URLEncoder.encode(str, "UTF-8");
			} catch (Exception e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
			}
		}
		return content;
	}
	
	public static String decode(String str) {
		String content ="";
		if(StringUtils.isNotBlank(str)) {
			try {
				content = str;
				//content = URLDecoder.decode(str, "UTF-8");
				return content;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return content;
	}
	
	//上面是一个文件一条记录的增删改查，下面是一个文件多条记录的增删改查
	/**
	 *       新增，该功能可能并发，虽然基本上不可能，但是还是需要考虑下的，用最简单的办法
	 * @param object 对象
	 * @param xmlPath 路径
	 * @return
	 */
	public synchronized static boolean add(Object object,String xmlPath) {
		//1、先查看这个文件是否存在
		try {
			Class clazz = object.getClass();
			Object obj = select(clazz, xmlPath);
			if(obj==null) {
				//这里表明文件还不存在，直接保存
				save(object, xmlPath);
			}else {
				//这里表明是在后面添加
				Document document = load(xmlPath);
				if (document == null) {
					return false;
				}
				Element root = document.getRootElement();
				String rootName = clazz.getSimpleName().toLowerCase();
				Element content = addElement(root, rootName, "");
				for (Field declaredField : clazz.getDeclaredFields()) {
					declaredField.setAccessible(true);
					//获取属性名字
					String name = declaredField.getName();
					//获取属性值
					String value = (String) declaredField.get(object);
					addElement(content, name,encode(value));
				}
				saveXmlFile(document, xmlPath);
			}
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	   *     查询一个xmlo文件中的所有内容
	 * @param clazz
	 * @param xmlPath
	 * @return
	 */
	public static <T> List<T> selects(Class<T> clazz, String xmlPath) {
		try {
			Document document = load(xmlPath);
			if (document == null) {
				return null;
			}
			Element root = document.getRootElement();
			List<T> objs = new ArrayList<T>();
			
			List<Element> list = root.elements();
			// 遍历p元素
			for (Element p : list) {
				T obj = clazz.newInstance();
				// 获取每个p元素中的name元素
				for (Field declaredField : clazz.getDeclaredFields()) {
					declaredField.setAccessible(true);
					String name = declaredField.getName();
					List<Element> childs = p.elements();
					for (Element p2 : childs) {
						if (p2.getName().equals(name)) {
							Class<?> z = declaredField.getType();
							String value = (String) p2.getData();
							if(value==null) {
								value="";
							}
							declaredField.set(obj,decode(value));
						}
					}
				}
				objs.add(obj);
			}
			return objs;
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * xml更新 其实内部只有一条记录
	 * @param object 要更新的对象
	 * @param xmlPath 路径
	 * @return
	 */
	public static boolean updateVisibleById(String id,String visible,String xmlPath) {
		try {
			Document document = load(xmlPath);
			if (document == null) {
				return false;
			}
			Element root = document.getRootElement();
			List<Element> list = root.elements();
			for (Element p : list) {
				// 获取每个p元素中的name元素
				// 这里就找到了要修改的元素
				List<Element> childs = p.elements();
				int flag= 0;
				for (Element p2 : childs) {
					String name  =p2.getName();
					if("id".equals(name)) {
						String value = (String) p2.getData();
						if(id.equals(value)) {
							//这里就相当于已经选好了
							flag=1;
						}
					}
					if(flag==1) {
						if("visible".equals(name)) {
							p2.setText(encode(visible));
						}
					}
				}
				if(flag==1) {
					break;
				}
			}
			return saveXmlFile(document, xmlPath);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return false;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String xmlPath = "E:/data/table/replys/753005780130594816.xml";
		updateVisibleById("763187762240159744", "10", xmlPath);
		
	}
	
}
