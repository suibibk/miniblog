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
    <title>登录页面</title>
    <script src="/js/jquery.min.js"></script>
    <script src="/js/md5.min.js"></script>
    <style type="text/css">
*{margin:0px;padding:0px;}
body{
	background-color:#000;
	font-family:"微软雅黑";
}
#login{
	width:400px;
	height:260px;
	margin:0px auto;
	background-color:#fff;
	margin-top:200px;
	border-radius:5px;
	box-shadow:#666 5px 5px 0px 0px;
}
#title{
	width:100%;
	height:50px;
	background-color:#3fa1a7;
	text-align:center;
	line-height:50px;
	font-size:25px;
	color:#fff;
	border-radius: 5px 5px 0px 0px;
}
#tip{
	width:100%;
	height:30px;
	text-align:center;
	line-height:30px;
	font-size:18px;
	color:red;
}
.username_password{
	width:100%;
	height:100px;
	background-color:#ccc;
	font-size:18px;
}	

.div1{
	float:left;
	width:20%;
	height:100%;
	text-align:right;
	line-height:50px;
}
.div2{
	width:100%;
	height:100%;
	text-align:left;
	line-height:50px;
	text-align:center;
}
.div2 input{
	width:80%;
	height:40px;
	border-radius:20px;
	padding-left:10px;
	font-size:18px;
}
#submit{
	width:100%;
	height:50px;
	text-align:center;
	line-height:50px;
}
#submit input{
	width:70px;
	height:40px;
	border-radius:20px;
	background-color:#3fa1a7;
	color:#fff;
}
	#check_code{
		padding: 5px;
		box-sizing: border-box;
		overflow: hidden;
		border: 1px solid #ddd;
		font-size: 14px;
		-webkit-appearance: none;
		border-radius: 0;
		margin-top:10px;
		width:150px;
	}
	#image_code{
		width: 75px;
		height: 30px;
		display: inline-block;
		vertical-align: middle;
	}
</style>
<script type="text/javascript">
var one =true;//防止二次提交
function login(){
	if(!one){
		return;
	}
	one=false;
	$("#tip").html("");
	var password = $("#password").val();
	if(password==""){
		$("#tip").html("密码不能为空");
		one=true;
		return;
	}
	var passwd = md5(password);
	var check_code = $("#check_code").val();
	if(check_code==""){
 	 	$("#tip").html("请输入验证码");
 	 	one=true;
 	 	return;
 	}
	$.ajax({ 
		url: "/mgr/toLogin",
		type: "POST",
		dataType:'json', 
		data : {"password":passwd,"check_code":check_code},
		error: function() {
           $("#tip").html("系统异常，请稍后再试！");
            one = true;
        },
        success:function(data){
             one = true;
             if(data.CODE==0){
             	window.location.href="/mgr";
             }else{
	             $("#tip").html(data.info);
             }
             if(data.CODE==2){
             	changeImage();
             }
        }
		
	});
}
function changeImage(){
		var timestamp = Date.parse(new Date());
		$("#image_code").attr("src","/getImage?time="+timestamp)
	}
</script>
</head>

<body>
	<div id="login">
		<div id="title">后台管理系统</div>
		<div id="tip"></div>
		<div id="username_password">
			<div class="div0">
			<div class="div2"><input type="text" id="check_code" class="reply_input" value="" placeholder="请输入验证码" maxlength="4">
			 <img src="/getImage?time=1" onclick="changeImage()" id="image_code"/></div>
				<div class="div2"><input type="password" maxlength="100" placeholder="请输入密码" id="password" name="password"/></div>
			</div>
		</div>
		<div id="submit"><input type="button" value="登录" onclick="login();"/></div>
	</div>
</body>

</html>