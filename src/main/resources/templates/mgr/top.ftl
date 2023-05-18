<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>随笔后台</title>
        <style>
            body{
                /*background: #edeef3;*/
                text-align: center;
                font-size: 25px;
                color: #fff;
            }
            #mybk{
            	position: fixed;
                left: 10px;
                font-size: 25px;
            }
            #mybk img{
            	width:40px;
            	height:40px;
            	vertical-align: middle;
            }
            #mybk span{
            	    display: inline-block;
    				vertical-align: middle;
            }
            #myblog{
                position: fixed;
                top: 0px;
                right: 10px;
                height: 70px;
                font-size: 16px;
                line-height:70px;
            }
            #myblog span{
                display: inline-block;
                cursor: pointer;
                height: 30px;
    			line-height: 30px;
            }
            #myblog img{
                width: 25px;
    height: 25px;
    border-radius: 100%;
    vertical-align: middle;
            }
            
        </style>
    </head>
    <body>
         <div id="mybk">&nbsp;迷你博客</span></div>
    <div id="myblog">
        <span onclick="parent.myblog();">首页</span>
        <span onclick="parent.logout();">&nbsp;退出</span>
    </div>
    
    </body>
</html>