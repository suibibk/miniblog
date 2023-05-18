<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>博客后台</title>
        <script src="/js/jquery.min.js"></script>
        <style>
            body{
                background: #fff;
                margin: 0px;
                padding: 0px;
            }
            ul{
                list-style: none;
                margin:0px;
                padding:0px;
            }
            li{
                  height: 50px;
				  line-height: 50px;
				  width: 100%;
				  text-align: center;
				  border-bottom: 1px solid #ccc;
				  font-weight: bold;
				  cursor: pointer;
            }
            li a{
                text-decoration: none;
                width: 100%;
    display: inline-block;
            }
            .choice{
                    color: #fff;
    				background: #3fa1a7;
            }
            .nochoice{
                color:#666;
            }
        </style>
    </head>
    <body>
    <ul>
                <li class="choice"><a onclick="parent.openChildDefaultIframe('/mgr/topic/0/0');" target="iframeRight">新建</a></li>
                <li><a onclick="parent.openChildDefaultIframe('/mgr/menus');" target="iframeRight">菜单</a></li>
                <li><a onclick="parent.openChildDefaultIframe('/mgr/topics');" target="iframeRight">笔记</a></li>
                <li><a onclick="parent.openChildDefaultIframe('/mgr/focals');" target="iframeRight">首图</a></li>
                <li><a onclick="parent.openChildDefaultIframe('/mgr/setUp');" target="iframeRight">设置</a></li>
    </ul>
    <script>
        $("li a").click(function(e) {
            e.stopPropagation();
            $(this).parent("li").removeClass("nochoice").addClass("choice");
            $(this).parent("li").siblings().removeClass("choice").addClass("nochoice");
        });
        $("li").first().addClass("choice");
    </script>
    </body>
</html>