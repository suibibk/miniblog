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
    <title>首焦图管理</title>
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
        #tablefocal{
            border-collapse:collapse;
            width:100%;
            border: 1px solid #ccc;
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
            background: #3fa1a7;
    		color: #fff;
    		height:30px;
    		line-height:30px;
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
			#span_music{
				display:none;
			}
    </style>
    <script>
        $(function(){
            getFocals();
            $("#type").change(function(){
            	searchFocals();
            	var value=$(this).val();
            	if(value=='03'){
            		$("#span_music").show();
            		$("#span_link").hide();
            	}else{
            		$("#span_music").hide();
            		$("#span_link").show();
            	}
            });
        });
        function addFocal(){
            //获取要添加的菜单信息
            var focal_name= $("#focal_name").val();
            var focal_url= $("#focal_url").val();
            var focal_link= $("#focal_link").val();
            var sort= $("#sort").val();
            var type= $("#type").val();
            if(type=="03"){
            	focal_link=$("#music_link").val();
            	if(focal_url==""){
            		focal_url="/image/mymusic.jpg";
            	}
            }
            $.ajax({
                url:'/mgr/addFocal',
                data:{"focal_name":focal_name,"focal_url":focal_url,"sort":sort,"focal_link":focal_link,"type":type},
                type:"POST",
                dataType:'json', //预期服务器返回的数据类型
                error: function() {
                    alert("添加出错！");
                },
                success:function(data){
                    if(data.CODE=='0'){
                        alert("成功");
                        getFocals();
                    }else{
                        alert("失败");
                    }
                }
            });
        }
 		function updateFocal(){
            //获取要添加的菜单信息
            var focal_name= $("#focal_name2").val();
            var focal_url= $("#focal_url2").val();
            var focal_link= $("#focal_link2").val();
            var sort= $("#sort2").val();
            var type= $("#type2").val();
            var editFocal= $("#editFocal").val();
            if(type=="03"){
            	focal_link=$("#music_link2").val();
            	if(focal_url==""){
            		focal_url="/image/mymusic.jpg";
            	}
            }
            $.ajax({
                url:'/mgr/updateFocal',
                data:{"id":editFocal,"focal_name":focal_name,"focal_url":focal_url,"sort":sort,"focal_link":focal_link,"type":type},
                type:"POST",
                dataType:'json', //预期服务器返回的数据类型
                error: function() {
                    alert("添加出错！");
                },
                success:function(data){
                    if(data.CODE=='0'){
                        alert("成功");
                         $(".top2").hide();
                        getFocals();
                    }else{
                        alert("失败");
                    }
                }
            });
        }

        function getFocals(){
            $.ajax({
                url:'/mgr/getFocals',
                data:{"type":$("#type").val()},
                type:"POST",
                dataType:'json', //预期服务器返回的数据类型
                error: function() {
                    alert("查询出错！");
                },
                success:function(data){
                    if(data.CODE=='0'){
                        //alert(data.info);
                        //初始化
                        var focals = data.FOCALS;
                        var str='<tr><th>序号</th><th>名称</th><th>类型</th><th class="mobile">头像</th><th class="mobile">排序</th><th>状态</th><th>操作</th></tr>';
                        var sort="";
                        var j = 0;
                        for(var i=0;i<focals.length;i++){
                            sort+='<option value="'+(i+1)+'">'+(i+1)+'</option>';
                            j=i+1;
                            var id=focals[i].id;
                            str+='<tr id="'+id+'">';
                            str+='<td>'+(i+1)+'</td>';
                            var focal_name = focals[i].focal_name;
                            str+='<td id="name">'+focal_name+'</td>';
                            var type = focals[i].type;
                            if(type=='01'){
                                str+='<td class="type">首图</td>';
                            }
                            if(type=='02'){
                                str+='<td class="type">友链</td>';
                            }
                            if(type=='03'){
                                str+='<td class="type">音乐</td>';
                            }
                            if(type=='04'){
                                str+='<td class="type">公告</td>';
                            }
                            var focal_url = focals[i].focal_url;
                            var focal_link = focals[i].focal_link;
                            str+='<td class="focal_url mobile"><img src="'+focal_url+'"></td>';
                            var s = focals[i].sort;
                            str+='<td  class="mobile">'+s+'</td>';
                            var visible = focals[i].visible;
                            if(visible=='0'){
                                str+='<td class="display">未发布</td>';
                            }
                            if(visible=='1'){
                                str+='<td class="display">已发布</td>';
                            }
                           
                            str+='<td class="displayFunc">';
                            if(visible=='0'){
                                str+='<button class="btntools displayFuncDo" onclick="visibleFocal(\''+id+'\',1,\''+type+'\')">发布</button>';
                            }
                            if(visible=='1'){
                                str+='<button class="btntools displayFuncDo" onclick="visibleFocal(\''+id+'\',0,\''+type+'\')">取消</button>';
                            }
                            
                            str+='<button class="btntools"  onclick="editFocal(\''+id+'\',\''+focal_name+'\',\''+focal_url+'\',\''+focal_link+'\',\''+s+'\',\''+type+'\')">编辑</button>';
                            str+='<button class="btntools" onclick="deleteFocal(\''+id+'\',\''+type+'\')">刪除</button>';
                            str+='</td>';
                            str+='</tr>';
                        }
                        //alert(str);
                        $("#tablefocal").html(str);
                        sort+='<option selected value="'+(j+1)+'">'+(j+1)+'</option>';
                        $("#sort").html(sort);
                        $("#sort2").html(sort);
                        //修改
                    }else{
                        alert("失败");
                    }
                }
            });
        }


        function deleteFocal(id,type){
            if(!window.confirm("删除后将不可恢复？")){
                return;
            }
            $.ajax({
                url:'/mgr/deleteFocal',
                data:{"id":id,"type":type},
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
                        alert("失败");
                    }
                }
            });
        }


        function visibleFocal(id,flag,type){
            $.ajax({
                url:'/mgr/visibleFocal',
                data:{"id":id,"visible":flag,"type":type},
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
                        $("#"+id+" .displayFunc .displayFuncDo").attr("onclick","visibleFocal(\'"+id+"\',"+newflag+")");
                    }else{
                        alert("失败");
                    }
                }
            });
        }
        
        
         function editFocal(id,focal_name,focal_url,focal_link,sort,type){
            $("#editFocal").val(id);
            $("#focal_name2").val(focal_name);
            if(focal_url!=''){
		           	 $("#focal_url2").val(focal_url);
		           	  $("#focal_url2_span").css("background-image","url("+focal_url+")");
		            }
		            
            if(type=="03"){
            	 $("#span_music2").show();
            	 $("#span_link2").hide();
            	 if(focal_link!=''){
		           	 $("#music_link2").val(focal_link);
		           	  $("#music_link2_span").css("background-image","url("+focal_link+")");
		            }
            }else{
            	$("#span_music2").hide();
            	$("#span_link2").show();
            	$("#focal_link2").val(focal_link);
            }
            $("#type2").val(type);	
            $("#sort2").val(sort);
            $(".top2").show();

        }

        
        function searchFocals(){
        	getFocals();
        }
        
    </script>
</head>
<body>
<input type="hidden" value="" id="editFocal">
<div id="menus">
<div class="top">
<span>类型：<select id="type">
<option value="01">首图</option>
<option value="04">公告</option>
</select></span>

<span>名称：<input id="focal_name" type="text" maxlength="100"  placeholder="请输入名称"/></span>
<span>首图：<#assign id = "focal_url"><#include "/common/uploadFile.ftl"></span>
<span id="span_link">跳转链接：<input id="focal_link" type="text" maxlength="255" placeholder="请输入跳转链接"/></span>
<span>排序：<select id="sort"></select></span>
<span><button onclick="addFocal()">添加</button></span>
</div>

<div class="top top2">
<span>类型：<select id="type2">
<option value="01">首图</option>
<option value="04">公告</option>
</select></span>

<span>名称：<input id="focal_name2" type="text" maxlength="100"  placeholder="请输入名称"/></span>
<span>首图：<#assign id = "focal_url2"><#include "/common/uploadFile.ftl"></span>
<span id="span_link2">跳转链接：<input id="focal_link2" type="text" maxlength="255" placeholder="请输入跳转链接"/></span>
<span>排序：<select id="sort2"></select></span>
<span><button onclick="updateFocal()">修改</button></span>
</div>

<div class="top">
温馨提醒：首图尺寸：700*350
<table id="tablefocal" border="1px">



</table>
</div>
</body>
</html>