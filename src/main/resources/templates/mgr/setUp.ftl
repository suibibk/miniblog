<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
        <meta name="apple-mobile-web-app-capable" content="yes">
        <meta name="apple-mobile-web-app-status-bar-style" content="black">
        <meta name="format-detection" content="telephone=no">
        <meta http-equiv="Expires" CONTENT="-1">
        <meta http-equiv="Cache-Control" CONTENT="no-cache">
        <meta http-equiv="Pragma" CONTENT="no-cache">
    <title>个人中心</title>
    <script src="/js/jquery.min.js"></script>
     <script src="/js/md5.min.js"></script>
    <style>
    body{
	        color: #333;
	    font-size: 16px;
	    }
        #menus{
            width:95%;
            margin: 0px auto;
        }
        .top{
            margin-bottom: 10px;
        }
        .top2{
           display: none;
        }
        span{
            display: inline-block;
            margin-right: 10px;
            margin-bottom:10px;
        }
        span input,select,button{
            height:95%;
        }
        select{
        	    min-width: 80px;
			    padding: 8px 10px;
			    border: 1px solid #ddd;
			    border-radius: 5px;
			    outline: none;
			    background: #fff;
        }
        input{
        	    padding: 8px 10px;
			    border: 1px solid #ddd;
			    border-radius: 5px;
			    outline: none;
			    box-sizing: border-box;
			    width:230px;
        }
        button{
        	border: none;
		    min-width: 50px;
		    padding: 8px 20px;
		    color: #fff;
		    background: #3fa1a7;
		    border-radius: 5px;
		    cursor: pointer;
		    outline: none;
        }
        #tablefocal{
            border: 1px solid #666;
            border-collapse:collapse;
            width:100%;
            border: 1px solid #999;
        }
        #tablefocal tr{
            text-align: center;
        }
        #tablefocal tr td{
            height: 100px;
        }
        #tablefocal tr td img{
            height: 90%;
        }
        #link_span{
            display: none;
        }

        #link_span2{
            display: none;
        }
        .remark{
            width:20%;
        }
.btntools{
        	height:30px;
        	line-height:30px;
        	padding:0px;
        	color: #187e68;
		    background: #fff;
		    border: 1px solid #187e68;
		    border-radius: 5px;
		    margin-bottom:2px;
		    margin-top:2px;
		    margin-left:2px;
		    margin-right:2px;
        }
        #tablefocal tr:nth-child(odd){
        	background:#eee;
        } 
	 #name{
        	    word-break: break-word;
        }
		#tablefocal tr:nth-child(even){
			background:#fff;
		} 
         #tablefocal th{
            background: #2fb295;
    		color: #fff;
    		height:30px;
    		line-height:30px;
        }
        .pd{
        	display:none;
        }
        .lefttd{
        	text-align:right;
        	padding-right:20px;
        	box-sizing: border-box;
        }
          .righttd{
        	text-align:left;
        	padding-left:20px;
        	box-sizing: border-box;
        }
        @media screen and (max-width: 750px) {
            	.mobile{
            		display:none;
            	}
            	#tablefocal{
            		table-layout:fixed;
            	}
        	}
			@media screen and (min-width: 750px) {
				.mobile{
				
				}
			}
			
	#userinfo{
		width:100%;
		text-align:center;
		margin-top:20px;
	}	
	.infos{
		margin-top:20px;
		margin-bottom:20px;
	}
	#selfUser{
		width:100%;
		text-align:center;
		margin-top:30px;
	}
    </style>
    <script>

        var two = true;
        function saveSetUp(){
        if(!two){
	       	return ;
	       }
       two =false;
      var blog_name  = $("#blog_name").val();
	  var copyright = $("#copyright").val();
	  var  beian_num= $("#beian_num").val();
	  var  phone= $("#phone").val();
	  var  name= $("#name").val();
	  var  qq=  $("#qq").val();
	  var email = $("#email").val();
	  var desc =  $("#desc").val();
	  var code =  $("#code").val();
	  var password1 =  $("#password1").val();
	  var password2 =  $("#password2").val();
	  
      if(password1!=""||password2!=""){
          if(password1!=password2){
          	 alert("两次密码不一致");
          	 two = true;
          	 return;
          }
      	 password1 = md5(password1);
      	 password2 = md5(password2);
      	 if(!window.confirm("确定要修改密码？")){
      	 		two = true;
		     return;
		 }
      }
            $.ajax({
                url:'/mgr/saveSetUp',
                data:{
                "blog_name":blog_name,
                "copyright":copyright,
                "beian_num":beian_num,
                "phone":phone,
                "qq":qq,
                "name":name,
                "email":email,
                "desc":desc,
                "code":code,
                "password1":password1,
                "password2":password2
                },
                dataType:'json', //预期服务器返回的数据类型
                type:"POST",
                error: function() {
                    alert("修改失败！");
                     two = true;
                },
                success:function(data){
                   alert(data.info);
                    two = true;
                }
            });
        }
        function changePasswd(){
            if($(".pd").css("display")=="none"){
            	$(".pd").show();
            	$("#xg").html("取消修改");
            }else{
            	$(".pd").hide();
            	$(".pd input").val("");
            	$("#xg").html("修改密码");
            }
        	
        }
    </script>
</head>
<body>
<div id="userinfo">
	<table style="display: inline-table; line-height: 50px;">
		<tr>
			<td class="lefttd">博客名称</td>
			<td class="righttd"><input id = "blog_name"  type="text" value="${setUp.blog_name!''}"></td>
		</tr>
		<tr>
			<td class="lefttd">CopyRight</td>
			<td class="righttd"><input id = "copyright"  type="text" value="${setUp.copyright!''}"></td>
		</tr>
		<tr><td class="lefttd">备案号</td><td class="righttd"><input id = "beian_num"  type="text" value="${setUp.beian_num!''}"></td></tr>
		<tr><td class="lefttd">手机</td><td class="righttd"><input id = "phone"  type="text" value="${setUp.phone!''}"></td></tr>
		<tr><td class="lefttd">姓名</td><td class="righttd"><input id = "name"  type="text" value="${setUp.name!''}"></td></tr>
		<tr><td class="lefttd">QQ</td><td class="righttd"><input id = "qq"  type="text" value="${setUp.qq!''}"></td></tr>
		<tr><td class="lefttd">邮箱</td><td class="righttd"><input id = "email"  type="text" value="${setUp.email!''}"></td></tr>
		<tr><td class="lefttd">个人介绍</td><td class="righttd"><input id = "desc"  type="text" value="${setUp.desc!''}"></td></tr>
	</table>
	<div class="infos"><button onclick="changePasswd()" id="xg">修改密码</button></div>
	<div class="infos pd">重置密码：<input id = "password1"  type="text" value=""  maxlength="100" ></div>
	<div class="infos pd">确认密码：<input id = "password2"  type="text" value=""  maxlength="100" ></div>
	<div class="infos"><button onclick="saveSetUp()">保存</button></div>
</div>
</body>
</html>