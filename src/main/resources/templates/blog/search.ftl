<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no">
        <meta name="apple-mobile-web-app-capable" content="yes">
        <meta name="apple-mobile-web-app-status-bar-style" content="black">
        <meta name="format-detection" content="telephone=no">
        <meta name="mobile-agent" content="format=html5;url=https://www.suibibk.com/search?value=${value}">
        <meta http-equiv="mobile-agent" content="format=html5; url=https://www.suibibk.com/search?value=${value}">
        <!-- End of Baidu Transcode -->
        <meta name="description" content="${value}">
        <meta name="keywords" content="个人随笔,${value}">
        <meta http-equiv="Expires" CONTENT="-1">
        <meta http-equiv="Cache-Control" CONTENT="no-cache">
        <meta http-equiv="Pragma" CONTENT="no-cache">
        
        <title>${value}</title>
        <script src="/js/jquery.min.js"></script>
        <script src="/js/Common.js?timestamp=2019022529"></script>
        <link href="/css/style.css?time=34" rel="stylesheet" type="text/css">
        <link href="/icons/iconfont/iconfont.css?v=4" rel="stylesheet" type="text/css">
        <script src="/icons/iconfont/iconfont.js?v=4"></script>
        <script async src="https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
    </head>
    <body>
    <#include "/common/top.ftl">
    
    <div id="all">
    	<div id="right">
    		<#include "/common/userMenu.ftl">
    	</div>
    	
    	<div id="left">
    		<div id="path">
	    		 <span>当前位置：</span>
	    		 <a href="/"><img src="/image/home.png"><span>首页</span></a>
	    		 <img src="/image/right.png">
	    		 <a href="/search?value=${value}"><span>${value}</span></a>
    		 </div>
    		 <div id="topic">
					<#if topics??>
                        <#list topics as topic>
                            <div class="topic">
                                <a href="/topic/${topic.id}">
	                                <div class="title">${topic.title}</div>
	                                <div class="desc">
	                                	 <#if topic.topicurl??>
		                                	  <#if topic.topicurl!="">
		                                	  	<div class="desc1 desc11"><xmp>${topic.description}</xmp></div>
	                                			<div class="desc2 desc22"><img src="${(topic.topicurl)!"/image/suolue.png"}"></div>
	                                			<#else>
			                                	 	<div class="desc1 desc111"><xmp>${topic.description}</xmp></div>
		                                	  </#if>
	                                	 	<#else>
	                                	 	<div class="desc1 desc111"><xmp>${topic.description}</xmp></div>
	                                	 </#if>
	                                </div>
                                </a>
                                <div class="info">
                                    <a href="/menu/${topic.menuid}"><img src="${topic.menu_img!'/image/txt.png'}">&nbsp;<span>${topic.menu_name!''}</span></a>
                                    <span>&nbsp;${topic.create_datetime}</span>
                                </div>
                            </div>
                        </#list>
                    </#if>
                    
                     <div id="tool">
	                	<script>
		                	 var nowPage = '${page}';
					         var page_code='${PAGE_CODE}';
					         function first(){
					        	window.location.href="/search?value=${value}&page=1";
					         }
					         function prev(){
						        window.location.href="/search?value=${value}&page="+(parseInt(nowPage)-1);
					         }
					         function next(){
						        window.location.href="/search?value=${value}&page="+(parseInt(nowPage)+1);
					         }
					         function last(){
					        	window.location.href="/search?value=${value}&page="+page_code;
					         }
	                	</script>
	                	<#if PAGE_CODE!=0>
	                		<div id="page_code"><span onclick="first()">首页</span><span onclick="prev()"><</span><span id="now_page">${page}/${PAGE_CODE}</span><span>${TOTAL!'0'}</span><span onclick="next()">></span><span onclick="last()">尾页</span></div>
	                	</#if>
                	</div>
                	
               </div>
    		<#include "/common/bottom.ftl">
    	</div>
    </div>
    </body>
</html>