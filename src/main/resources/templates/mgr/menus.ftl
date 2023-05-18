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
    <title>菜单管理</title>
    <script src="/js/jquery.min.js"></script>
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
        #tablemenu{
            border-collapse:collapse;
            width:100%;
            border: 1px solid #ccc;
        }
        #tablemenu tr{
            text-align: center;
        }
        #tablemenu  tr td{
            height: 100px;
        }
        #tablemenu  tr td img{
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
        #name{
        	    word-break: break-word;
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
        #tablemenu tr:nth-child(odd){
        	background:#eee;
        } 

		#tablemenu tr:nth-child(even){
			background:#fff;
		} 
         #tablemenu th{
            background: #3fa1a7;
    		color: #fff;
    		height:30px;
    		line-height:30px;
        }
        @media screen and (max-width: 750px) {
            	.mobile{
            		display:none;
            	}
            	#tablemenu{
            		table-layout:fixed;
            	}
        	}
			@media screen and (min-width: 750px) {
				.mobile{
				
				}
			}
    </style>
    <script>
        $(function(){
            getMenus();
        });
        function addMenus(){
            //获取要添加的菜单信息
            var type= $("#type").val();
            var name= $("#name").val();
            var remark= $("#remark").val();
            var link= $("#link").val();
            var sort= $("#sort").val();
            var menuid= $("#editMenu").val();
            var parent_id= $("#parent1").val();
            imgurl = "";
            if(menuid!=""){
                type= $("#type2").val();
                name= $("#name2").val();
                remark= $("#remark2").val();
                link= $("#link2").val();
                sort= $("#sort2").val();
               	imgurl =$("#imgurl2").val()
               	parent_id= $("#parent2").val();
            }else{
               imgurl =$("#imgurl").val();
            }
            if(name==""){
                alert("菜单名称不能为空");
                return;
            }
            if(type=="2"){
                if(link==""){
                    alert("外链连接不能为空");
                    return;
                }
                if(link.length>255){
                    alert("外链过长");
                    return;
                }
            }
           
            if(link==""){
                link="-1";
            }
            //alert(type+";"+name+";"+remark+";"+imgurl+";"+sort);
            $.ajax({
                url:'/mgr/addMenus',
                data:{"menuid":menuid,"type":type,"name":name,"remark":remark,"imgurl":imgurl,"link":link,"sort":sort,"parent_id":parent_id},
                type:"POST",
                dataType:'json', //预期服务器返回的数据类型
                error: function() {
                    alert("添加出错！");
                },
                success:function(data){
                    if(data.CODE=='0'){
                        alert("成功");
                        getMenus();
                        if(menuid!="0"){
                            $(".top2").hide();
                            $("#editMenu").val("");
                        }else{
                            $("#name").val("");
                            $("#remark").val("");
                            $("#link").val("");
                           // $("#imgurl").val("");
                        }
                    }else{
                        alert("执行失败");
                    }
                    $("#imagePath").val("-1");
                }
            });
        }

        function uploadFile(fileid,formid) {
            var dom = document.getElementById(fileid);
            if(dom.value!="") {
                name = dom.value;
                name = name.toLowerCase();
                //用户只可以上传图片：jpg/jpeg/png/gif 音频mp3 视频 mp4
                if (name.indexOf("jpg") != -1 || name.indexOf("jpeg") != -1 || name.indexOf("png") != -1 || name.indexOf("gif") != -1 || name.indexOf("bmp") != -1) {
                    var formData = new FormData($("#"+formid)[0]);
                    $.ajax({
                        url: '/mgr/uploadImg' ,
                        type: 'POST',
                        data: formData,
                        async: false,
                        cache: false,
                        contentType: false,
                        processData: false,
                        success: function (data) {
                            if(data.success=="1"){
                                $("#imagePath").val(data.url);
                                return;
                            }
                            $("#imagePath").val("-1");
                        },
                        error: function () {
                            alert("图片上传失败");
                            $("#imagePath").val("-1");
                        }
                    });
                }
            }
        }

        function getMenus(){
            $.ajax({
                url:'/mgr/getMenus',
                data:{},
                type:"POST",
                dataType:'json', //预期服务器返回的数据类型
                error: function() {
                    alert("查询出错！");
                },
                success:function(data){
                        //初始化
                        var menus = data.MENUS;
                        var str='<tr><th>序号</th><th>名称</th><th class="mobile">备注</th><th class="mobile">头像</th><th>类型</th><th  class="mobile">排序</th><th>状态</th><th>操作</th></tr>';
                        var sort="";
                        var parents='<option value="0">随笔</option>';
                        var j = 0;
                        for(var i=0;i<menus.length;i++){
                            sort+='<option value="'+(i+1)+'">'+(i+1)+'</option>';
                            var type= menus[i].type;
                            var id=menus[i].id;
                            var parent_id=menus[i].parent_id;
                            if(parent_id==null){
                            	parent_id="0";
                            }
                            var name = menus[i].name;
                            
                            if(type=="0"){
                            	parents+='<option value="'+id+'">'+name+'</option>';
                            }
                            j=i+1;
                            str+='<tr id="'+id+'" class="menu_tr parent_'+parent_id+'">';
                            str+='<td>'+(i+1)+'</td>';
                            str+='<td id="name">'+name+'</td>';
                            var remark = menus[i].remark;
                            str+='<td class="remark mobile">'+remark+'</td>';
                            str+='<td  class="mobile"><img src="'+menus[i].imgurl+'"></td>';
                            var link = menus[i].link;
                            var imgurl = menus[i].imgurl;
                             if(type=='0'){
                                str+='<td>目录</td>';
                            }
                            if(type=='1'){
                                str+='<td>标签</td>';
                            }
                            if(type=='2'){
                                str+='<td><a href="'+link+'">外链</a></td>';
                            }
                            var s = menus[i].sort;
                            str+='<td  class="mobile">'+s+'</td>';
                            var visible = menus[i].visible;
                            if(visible=='0'){
                                str+='<td class="display">未发布</td>';
                            }
                            if(visible=='1'){
                                str+='<td class="display">已发布</td>';
                            }
                            str+='<td class="displayFunc">';
                            if(visible=='0'){
                                str+='<button class="btntools displayFuncDo"  onclick="visibleMenu(\''+id+'\',1)">发布</button>';
                            }
                            if(visible=='1'){
                                str+='<button class="btntools displayFuncDo"  onclick="visibleMenu(\''+id+'\',0)">取消</button>';imgurl
                            }
                            str+='<button class="btntools"  onclick="editMenu(\''+id+'\',\''+type+'\',\''+link+'\',\''+name+'\',\''+s+'\',\''+remark+'\',\''+imgurl+'\',\''+parent_id+'\')">编辑</button>';
                            str+='<button class="btntools"  onclick="deleteMenu(\''+id+'\')">刪除</button>';
                            str+='</td>';
                            str+='</tr>';
                        }
                        //alert(str);
                        $("#tablemenu").html(str);
                        sort+='<option selected value="'+(j+1)+'">'+(j+1)+'</option>';
                        $("#sort").html(sort);
                        $("#parent1").html(parents);
                        //修改
                        $("#sort2").html(sort);
                        $("#parent2").html(parents);
                        $("#parent3").html(parents);
                }
            });
        }


        function deleteMenu(id){
            if(!window.confirm("删除后将不可恢复？")){
                return;
            }
            $.ajax({
                url:'/mgr/deleteMenu',
                data:{"menuid":id},
                type:"POST",
                dataType:'json', //预期服务器返回的数据类型
                error: function() {
                    alert("刪除失敗！");
                },
                success:function(data){
                    if(data.CODE=='0'){
                        alert("成功");
                        $("#"+id).hide();
                    }else{
                        alert("删除失败");
                    }
                }
            });
        }


        function editMenu(id,type,link,name,s,remark,imgurl,parent_id){
            $("#editMenu").val(id);
            //alert(id+type+link+name+s+remark);
            $("#type2").val(type);
            $("#sort2").val(s);
            if(type=="2"){
                $("#link_span2").show();
                $("#link2").val(link);
            }else{
                $("#link_span2").hide();
            }
            $("#name2").val(name);
            $("#remark2").val(remark);
            if(imgurl!=''){
           	 $("#imgurl2").val(imgurl);
           	  $("#imgurl2_span").css("background-image","url("+imgurl+")");
            }
              
              $("#parent2").val(parent_id);
              
            $(".top2").show();

        }


        function visibleMenu(id,flag){
            $.ajax({
                url:'/mgr/visibleMenu',
                data:{"menuid":id,"visible":flag},
                type:"POST",
                dataType:'json', //预期服务器返回的数据类型
                error: function() {
                    alert("操作失敗！");
                },
                success:function(data){
                    if(data.CODE=='0'){
                       alert("成功");
                        var str="";
                        var msg="";
                        var newflag =1;
                        if(flag==0){
                            str="未发布";
                            msg="发布";
                            newflag=1;
                        }
                        if(flag==1){
                            str="已发布";
                            msg="取消";
                            newflag=0;
                        }
                        $("#"+id+" .display").html(str);
                        $("#"+id+" .displayFunc .displayFuncDo").html(msg);
                        $("#"+id+" .displayFunc .displayFuncDo").attr("onclick","visibleMenu(\'"+id+"\',"+newflag+")");
                    }else{
                        alert("执行失败");
                    }
                }
            });
        }
        
        function selectMenu(){
        	var parent_id=$("#parent3").val();
        	if(parent_id=="0"){
        		//全部显示
        		$(".menu_tr").show();
        	}else{
        		$(".menu_tr").hide();
        		$(".parent_"+parent_id).show();
        	}
        }
    </script>
</head>
<body>
<input type="hidden" value="" id="editMenu">
<input type="hidden" value="-1" id="imagePath">
<div id="menus">
<div class="top">
<span>类型：<select id="type"><option value="0">目录</option><option value="1" selected>标签</option><option value="2">外链</option></select></span>
<span id="link_span">外链链接:<input id="link" type="text" maxlength="255" placeholder="请输入外链链接（255）" /></span>
<span>名称：<input id="name" type="text" maxlength="20"  placeholder="请输入菜单名称（20）"/></span>
<span>备注：<input id="remark" type="text" maxlength="200" placeholder="请输入菜单备注（200）"/></span>
<span>图片：<#assign id = "imgurl"><#include "/common/uploadFile.ftl"></span>
<!--https://myforever.cn/itmgr/dhp/images/2.png-->
 <span>排序：<select id="sort"></select></span>
 <span>父级：<select id="parent1"></select></span>
    <span><button onclick="addMenus()">添加</button></span>
</div>
<div class="top top2">
        <span>类型：<select id="type2"><option value="0">目录</option><option value="1">标签</option><option value="2">外链</option></select></span>
        <span>名称：<input id="name2" type="text" maxlength="20" placeholder="请输入菜单名称（20）"/></span>
        <span>备注：<input id="remark2" type="text" maxlength="200" placeholder="请输入菜单备注（200）"/></span>
    	<span>图片：<#assign id = "imgurl2"><#include "/common/uploadFile.ftl"></span>
        <span id="link_span2">外链链接:<input id="link2" maxlength="255" type="text" placeholder="请输入外链链接（255）" /></span>
        <span>排序：<select id="sort2"></select></span>
        <!--https://myforever.cn/itmgr/dhp/images/2.png-->
         <span>父级：<select id="parent2"></select></span>
        <span><button onclick="addMenus()">修改</button></span>
</div>

<div class="top">
<span>父级：<select id="parent3" onchange="selectMenu()"></select></span>
    <span><button onclick="selectMenu()">查询</button></span>
</div>
<table id="tablemenu"  border="1px" cellspacing="0" cellpadding="0">



</table>
</div>
<script>
    $("#type").change(function(){
        var value = $(this).val();
        if(value==2){
            $("#link_span").show();
        }else{
            $("#link_span").hide();
        }
    });
    $("#type2").change(function(){
        var value = $(this).val();
        if(value==2){
            $("#link_span2").show();
        }else{
            $("#link_span2").hide();
        }
    });
</script>
</body>
</html>