<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no">
        <meta name="apple-mobile-web-app-capable" content="yes">
        <meta name="apple-mobile-web-app-status-bar-style" content="black">
        <meta name="format-detection" content="telephone=no">
        <!-- End of Baidu Transcode -->
        <meta name="description" content="轻博客记录自己的学习笔记、生活感想、美食照片、工作计划...">
        <meta name="keywords" content="随笔，个人随笔，笔记，技术笔记，生活笔记，随笔博客，技术博客，免费博客，IT技术，技术分享，个人博客,优秀的个人博客,个人网站,优秀的个人网站">
        <meta http-equiv="Expires" CONTENT="-1">
        <meta http-equiv="Cache-Control" CONTENT="no-cache">
        <meta http-equiv="Pragma" CONTENT="no-cache">
        
        <title>个人随笔_一个程序员的学习笔记</title>
        <script src="/js/jquery.min.js"></script>
        <script src="/js/Common.js?timestamp=2019022529"></script>
        <link href="/css/style.css?time=45" rel="stylesheet" type="text/css">
        <link href="/icons/iconfont/iconfont.css?v=5" rel="stylesheet" type="text/css">
         <script src="/js/swiper-3.4.2.min.js"></script>
          <link href="/css/swiper-3.4.2.min.css" rel="stylesheet" type="text/css">
        <script src="/icons/iconfont/iconfont.js?v=4"></script>
    <style>
    </style>
    
    </head>
    <body>
    <img style="top: 0;height: 0;position: fixed;" src="/image/logo.png">
    <#include "/common/top.ftl">
    <div id="all">
    	<div id="right">
    		<#include "/common/userMenu.ftl">
    	</div>
    	
    	<div id="left">
    		<div id="focal">
                    <div class="swiper-container">
                        <div class="swiper-wrapper">
		                     <#if focals??>
		                        <#list focals as focal>
			                        <div class="swiper-slide">
			                             <a href="${focal.focal_link}"><img src="${focal.focal_url}"></a>
			                        </div>
		                        </#list>
		                    </#if>
                        </div>
                        <div id="focal_pa" class="pagination"></div>
                    </div>
                    
                     <script>
				        var mySwiper = new Swiper('.swiper-container', {
				            loop: true,
				            autoplay: 2500,
				            pagination: '.pagination',
				            paginationType: 'bullets',
				            paginationClickable: true,
				            autoplayDisableOnInteraction: false
				        });
				    </script>
            </div>
            
            <div id="gg">
                	<div class="ggg">
                	 <#if ggs??>
                        <#list ggs as focal>
                        	<#if focal_index==0>
                        		<div class="gg" id="gg_${focal_index}" style="display:block"><a href="${focal.focal_link}"><div><svg class="icongg svg-icon" aria-hidden="true"><use xlink:href="#iconlaba2"></use></svg>${focal.focal_name}</div></a></div>
                        		<#else>
                        			<div class="gg"  id="gg_${focal_index}"><a href="${focal.focal_link}"><div><svg class="icongg svg-icon" aria-hidden="true"><use xlink:href="#iconlaba2"></use></svg>${focal.focal_name}</div></a></div>
                        	</#if>
                        </#list>
                   	 </#if>
            </div>
                	<script>
                		var ggi = $(".gg").length;
                		var gg_index=1;
                		var gd = true;
                		function showgg(){
                			if(gd){
	                			$(".gg").hide();       			
								$("#gg_"+gg_index).show();     
								gg_index++;
								if(gg_index==ggi) {
									gg_index=0;
								} 		
                			}
                		}
                		setInterval(showgg, 3500); //2分钟存1次草稿
                		$(".gg").mouseover(function(){
                			gd=false;
                			$(this).css("font-size","18px");
                		});
                		$(".gg").mouseout(function(){
                			gd=true;
                			$(this).css("font-size","16px");
                		});
                	</script>
                	
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
					        	window.location.href="/?page=1";
					         }
					         function prev(){
						        window.location.href="/?page="+(parseInt(nowPage)-1);
					         }
					         function next(){
						        window.location.href="/?page="+(parseInt(nowPage)+1);
					         }
					         function last(){
					        	window.location.href="/?page="+page_code;
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