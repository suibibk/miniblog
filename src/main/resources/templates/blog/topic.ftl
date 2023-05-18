<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="format-detection" content="telephone=no">
    <meta http-equiv="Expires" CONTENT="-1">
    <meta http-equiv="Cache-Control" CONTENT="no-cache">
    <meta http-equiv="Pragma" CONTENT="no-cache">
    <!-- End of Baidu Transcode -->
    <meta name="description" content="${title}">
    <meta name="keywords" content="${title}">
    <title>${title}</title>
    <link rel="stylesheet" href="/editormd/css/editormd.min.css"/>
    <link href="/css/style.css?time=35" rel="stylesheet" type="text/css">
    <script src="/js/jquery.min.js"/>"></script>
    <script src="/js/Common.js"></script>
    <link href="/icons/iconfont/iconfont.css?v=6" rel="stylesheet" type="text/css">
    <script src="/icons/iconfont/iconfont.js?v=4"></script>
    <script>
      var itemId = '${topicid}';
    </script>
    
</head>
<body>
<img style="top: 0;height: 0;position: fixed;" src="${topicurl}">
<input type="hidden" value="${topicid}" id="topicId">
 <#include "/common/top.ftl">
 	
    <div id="all">
    	<style>
    	 #ml{
    	 	 position: fixed;
			 top: 65px;
			 height:28px;
			 line-height:28px;
			 background: #fff;
			 right: 0;
			 z-index: 1;
			 padding-left: 3px;
			 padding-right: 3px;
			 box-sizing: border-box;
			 border-radius: 2px;
			 box-shadow: 0px 1px 3px #999;
			 background:#3fa1a7;
			 color:#fff;
			 cursor: pointer;
    	 }
    	 #mulu div:hover{
    	 	background:#f1f1f1;	
    	 }
    	 #mulu div a{
    	 	width:100%;
    	 	display:block;
    	 }
    	 #mulu{
    	 	 position: fixed;
			 top: 95px;
			 background: #fff;
			 right: -250px;
			 z-index: 1;
			 width: 250px;
			 overflow: auto;
			 max-height: 60%;
			 padding: 10px;
			 box-sizing: border-box;
			 border-radius: 2px;
			 box-shadow: 0px 1px 3px #999;
    	 }
    	 #mulu div{
    	 	 padding: 5px;
    	 	 box-sizing: border-box;
    	 }
    	 .reference-link{
			    padding-top: 80px;
	    }
    	</style>
    	<div id="ml" onclick="showml()">目录</div>
        <div id="mulu">
        </div>
        <script>
        function showml(){
        		 $("#mulu").animate({right:'0px'},400,"linear",function(){});
                 $("#ml").attr("onclick","closeml()")	;
        }
        function closeml(){
        		 $("#mulu").animate({right:'-250px'},400,"linear",function(){});
                 $("#ml").attr("onclick","showml()")	;
        }
        </script>
    	<div id="right">
    		<#include "/common/userMenu.ftl">
    	</div>
    	
    	<div id="left">
    	 	<div id="path">
	    		 <span>当前位置：</span>
	    		 <a href="/"><img src="/image/home.png"><span>首页</span></a>
	    		 <img src="/image/right.png">
	    		 <a href="/menu/${menu.id}"><span>${menu.name}</span></a>
	    		 <img src="/image/right.png">
	    		 <a href="/topic/${topicid}"><span>${title}</span></a>
    		 </div>
    		
			<div id="topic">
				<div id="topic_top">
			    	<div id="title">${title}</div>
			    	<div id="info">
				         <span id="updatetime">${create_datetime}</span>
			   	 	</div>
			   
			    	<div id="content" class="markdown-body editormd-html-preview">
			    		${content}
			    	</div>
			    	<div style="width:100%;text-align:center;">
			    		<span class="iconfont iconicon_yulan">&nbsp;${TOPIC_VISIT}</SPAN>
			    	</div>
				    <div id="preNext">
					    <#if pre=='HAVE'>
					    	<a href="/topic/${preId!''}"><div class="m3"><span style="color:#000;">上一篇:</span>&nbsp;${preTitle!''}</div></a>
					    	<#else>
					    		<div class="m3"><span style="color:#000;">上一篇:</span>&nbsp;无</div>
					    </#if>
					    <#if next=='HAVE'>
					    	<a href="/topic/${nextId!''}"><div class="m3"><span style="color:#000;">下一篇:</span>&nbsp;${nextTitle!''}</div></a>
					    	<#else>
						   		<div class="m3"><span style="color:#000;">下一篇:</span>&nbsp;无</div>
					    </#if>
				    </div>
			 </div>  
			 <#include "/common/bottom.ftl">
		</div>
    </div>
    <div id="bigimg"><img src=""/></div>
    <script>
    	$(function(){
    		$(".markdown-body img").click(function(){
				var obj = $(this);
				var src = obj.attr("src");
				$("#bigimg img").attr("src",src);
				$("#bigimg").show();
			});
			$("#bigimg").click(function(){
				$("#bigimg").hide();
			});
			
			var mulu = '';
			$(".reference-link").each(function(){
			   var obj = $(this);
			   var value = obj.attr("name");
			   var h3 = obj.parent("h3");
			   var h3Str = h3.html();
			   var h4 = obj.parent("h4");
			   var h4Str = h4.html();
			   var h5 = obj.parent("h5");
			   var h5Str = h5.html();
			   var h6 = obj.parent("h6");
			   var h6Str = h6.html();
			   var showName = value;
			   if(h6Str!=undefined){
			      showName="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+value;
			   }
			   if(h5Str!=undefined){
			      showName="&nbsp;&nbsp;&nbsp;&nbsp;"+value;
			   }
			   if(h4Str!=undefined){
			      showName="&nbsp;&nbsp;"+value;
			   }
			   if(h3Str!=undefined){
			     showName=value;
			   }
			   mulu+='<div><a href="#'+value+'">'+showName+'</a></div>';
			  
			});
			// console.log(mulu);
			$("#mulu").html(mulu);
    	});
    </script>
</body>
</html>