package com.suibibk.utils;
 
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * setConnectTimeout：设置连接超时时间，单位毫秒。

setConnectionRequestTimeout：设置从connect Manager(连接池)获取Connection 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。

setSocketTimeout：请求获取数据的超时时间(即响应时间)，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
 */
/**
 * http请求工具类
 * @author lwh
 * @date 20181130
 */
public class HttpUtils {
	private static final Integer  CONNECT_TIMEOUT = 5000;//接超时时间，单位毫秒
	private static final Integer  CONNECT_REQUEST_TIMEOUT = 1000;//从connect Manager(连接池)获取Connection 超时时间，单位毫秒。
	private static final Integer  SOCKET_TIMEOUT = 5000;//响应时间
	private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);
    /**
     * url get请求
     * @param url 访问的url
     * @return
     * @throws Exception
     */
    public static String doGet(String url) throws Exception {
    	log.info("请求的url:"+url);
    	HttpResponse response = doGet(url, null, CONNECT_TIMEOUT, CONNECT_REQUEST_TIMEOUT, SOCKET_TIMEOUT);
    	String result = getString(response);
    	log.info("返回的值："+result);
    	return result;
    }
    /**
     * url post请求
     * @param url 请求的url
     * @param json 传输的json信息，建议全部转换成json回来
     * @return
     * @throws Exception
     */
    public static String doPost(String url,String json) throws Exception {
    	log.info("请求的url:"+url);
    	HttpResponse response = doPost(url, json, CONNECT_TIMEOUT, CONNECT_REQUEST_TIMEOUT, SOCKET_TIMEOUT);
    	String result = getString(response);
    	log.info("返回的值："+result);
    	return result;
    }
	
	
    public static HttpResponse doGet(String path,Map<String, String> querys,Integer connectTimeout,Integer ConnectionRequestTimeout,Integer responseConnectTimeout)
            throws Exception {
        HttpClient httpClient = wrapClient(path);
        HttpGet request = new HttpGet(path);
        RequestConfig requestConfig = RequestConfig.custom()  
                .setConnectTimeout(connectTimeout).setConnectionRequestTimeout(ConnectionRequestTimeout)  
                .setSocketTimeout(responseConnectTimeout).build();  
        request.setConfig(requestConfig);  

        return httpClient.execute(request);
    }
    public static HttpResponse doPost(String path,
                                      Map<String, Object> bodys,Integer connectTimeout,Integer ConnectionRequestTimeout,Integer responseConnectTimeout)
            throws Exception {
        HttpClient httpClient = wrapClient(path);
        HttpPost request = new HttpPost(path);
        RequestConfig requestConfig = RequestConfig.custom()  
                .setConnectTimeout(5000).setConnectionRequestTimeout(1000)  
                .setSocketTimeout(5000).build();  
        request.setConfig(requestConfig);  
        if (bodys != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
            for (String key : bodys.keySet()) {
                nameValuePairList.add(new BasicNameValuePair(key, bodys.get(key).toString()));
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairList, "utf-8");
            formEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
            request.setEntity(formEntity);
        }
        return httpClient.execute(request);
    }
    public static HttpResponse doPost(String path,
           String  jsonBody,Integer connectTimeout,Integer ConnectionRequestTimeout,Integer responseConnectTimeout)
            		throws Exception {
    	HttpClient httpClient = wrapClient(path);
    	HttpPost request = new HttpPost(path);
    	RequestConfig requestConfig = RequestConfig.custom()  
                .setConnectTimeout(5000).setConnectionRequestTimeout(1000)  
                .setSocketTimeout(5000).build();  
        request.setConfig(requestConfig); 
    	if (jsonBody != null) {
    		@SuppressWarnings("deprecation")
			StringEntity entity = new StringEntity(jsonBody, "text/xml", "utf-8");
    		entity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
    		request.setEntity(entity);
    	}
    	return httpClient.execute(request);
}
 
   
    /**
     * 获取 HttpClient
     * @param path
     * @return
     */
    private static HttpClient wrapClient(String path) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        if (path != null && path.startsWith("https://")) {
            return sslClient();
        }
        return httpClient;
    }
 
    /**
     * 在调用SSL之前需要重写验证方法，取消检测SSL
     * 创建ConnectionManager，添加Connection配置信息
     * @return HttpClient 支持https
     */
    private static HttpClient sslClient() {
        try {
            // 在调用SSL之前需要重写验证方法，取消检测SSL
            X509TrustManager trustManager = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
            };
            SSLContext ctx = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
            ctx.init(null, new TrustManager[] { trustManager }, null);
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE);
            // 创建Registry
            RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT)
                    .setExpectContinueEnabled(Boolean.TRUE).setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM,AuthSchemes.DIGEST))
                    .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https",socketFactory).build();
            // 创建ConnectionManager，添加Connection配置信息
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            CloseableHttpClient closeableHttpClient = HttpClients.custom().setConnectionManager(connectionManager)
                    .setDefaultRequestConfig(requestConfig).build();
            return closeableHttpClient;
        } catch (KeyManagementException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
 
    public static String getString(HttpResponse httpResponse) throws IOException {
        HttpEntity entity = httpResponse.getEntity();
        String resp = EntityUtils.toString(entity, "UTF-8");
        return resp;
    }
    
  /*  public static void main(String[] args) throws Exception {
    	String url ="https://www.myforever.cn/itsb/blog/blogViewAction!getHotTopic.action?num=10";
    	System.out.println(doGet(url));
    	System.out.println(doPost(url,null));
	}*/
}
