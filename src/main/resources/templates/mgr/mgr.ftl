<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
        <meta name="apple-mobile-web-app-capable" content="yes">
        <meta name="apple-mobile-web-app-status-bar-style" content="black">
        <meta name="format-detection" content="telephone=no">
        <meta http-equiv="Expires" CONTENT="-1">
        <meta http-equiv="Cache-Control" CONTENT="no-cache">
        <meta http-equiv="Pragma" CONTENT="no-cache">
        <title>随笔后台</title>
        <script src="/js/jquery.min.js"></script>
        <style>
            *{
                margin: 0px;
                padding: 0px;
            }
            body{
                background: #fff;
            }
            #iframeTop{
                width: 100%;
                height: 70px;
                top:0px;
                position: absolute;
                box-sizing: border-box;
                BACKGROUND:#3fa1a7;
                COLOR:#FFF;
            }
            #iframeLeft {
                width: 15%;
                height: 90%;
                top:70px;
                left:0px;
                position: absolute;
                border: 1px solid #ccc;
                border-top:1px solid #fff;
                box-sizing: border-box;
                BACKGROUND: #fff;
            }
            #iframeRight{
                width: 85%;
                height: 90%;
                top:70px;
                right:0px;
                position: absolute;
                box-sizing: border-box;
                BACKGROUND: #FFF;
                 border-bottom: 1px solid #ccc;
            }
              
        </style>
        <script>
            function logout(){
                window.location.href="/logout?link=";
            }
            function myblog(){
                window.location.href="/";
            }
            
            
	   
	   function openChildIframe(iframeId,url){
		   $("#"+iframeId).attr("src",url); 
	   }
	   /**
	   *默认打开到intro界面
	   **/
	   function openChildDefaultIframe(url){
		  openChildIframe('iframeRight',url);
	   }
        </script>
    </head>
    <body>
    <iframe id="iframeTop" name="iframeTop" frameborder="0" scrolling="no" src="/mgr/top"></iframe>
    <iframe id="iframeLeft" name="iframeLeft" frameborder="0" scrolling="no"  src="/mgr/left"></iframe>
    <!--默认新建博文-->
    <iframe id="iframeRight" name="iframeRight" frameborder="0" src="/mgr/topic/0/0"></iframe>
    </body>
</html>