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
    <script src="/js/Common.js"></script>
    <style>
    body{
    	color: #333;
	    font-size: 16px;
    }
        #topics{
            width:95%;
            margin: 0px auto;
        }
        #top{
            width:100%;
            margin-bottom:10px;
        }
        #top1{
            height:100%;
            float: left;
            color: #4c4b4b;
            margin-bottom: 10px;
        }
        #top2 a{
            text-decoration: none;
        }
        select{
            height:100%;
            color: #4c4b4b;
        }
        button{
            height:100%;
            color: #4c4b4b;
        }
        select{
        	    min-width: 80px;
			    padding: 8px 10px;
			    border: 1px solid #ddd;
			    border-radius: 5px;
			    outline: none;
			    background: #fff;
        }
        button{
        	border: none;
		    min-width: 50px;
		    padding: 8px 20px;
		    color: #fff;
		    background:#3fa1a7;
		    border-radius: 5px;
		    cursor: pointer;
		    outline: none;
        }
        #top2{
            height: 100%;
            float: right;
            text-align: right;
            color: #551A8B;
            line-height: 30px;
        }
        #topictable{
            border-collapse:collapse;
            width:100%;
            border: 1px solid #ccc;
        }
        #topictable tr{
            text-align: center;
        }
        #topictable tr td{
            height: 50px;
        }
        .title{
            width:30%;
            text-align:left;
            padding-left:5px;
        }
        .title_div{
            /*height: 30px;
            line-height:30px;
            overflow:auto;*/
            word-wrap:break-word
            
        }
        #topictable th{
            background: #3fa1a7;
    		color: #fff;
    		height:30px;
    		line-height:30px;
        }
        .more{
            width: 100%;
            text-align: center;
            display: none;
            margin-top:10px;
        }
        .msg{
            width: 100%;
            text-align: center;
            display: none;
        }
        .btntools{
        	height:25px;
        	line-height:25px;
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
        #topictable tr:nth-child(odd){
        	background:#eee;
        } 

		#topictable tr:nth-child(even){
			background:#fff;
		} 
       /* .description{
            text-align: left;
            width:20%;
        }
        .description_div{
            height: 100%;
            overflow: auto;
        }*/
        
        
            @media screen and (max-width: 750px) {
            	.mobile{
            		display:none;
            	}
            	#topictable{
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
            getTopics();
            $("select").change(function(){
                $("#page").val("1");
                $("#topictable").html("<tr><th>序号</th><th>标题</th><th>状态</th><th  class='mobile'>创建时间</th><th  class='mobile'>更新时间</th><th>操作</th></tr>");
                getTopics();
            });
        });
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
                    if(data.CODE=='0'){
                        //alert(data.info);
                        //初始化
                        var menus = data.MENUS;
                        var str='<option value="-1">选择所属标签</option>';
                        for(var i=0;i<menus.length;i++){
                            if(menus[i].type=='1') {
                                str+='<option value="' + menus[i].id + '">' + menus[i].name + '</option>';
                            }
                        }
                        //alert(str);
                        $("select").html(str);
                    }else{
                        alert(data.info);
                    }
                }
            });
        }
        function more(){
            var page = $("#page").val();
            $("#page").val(parseInt(page)+1);
            $(".more").hide();
            getTopics();
        }
        function getTopics(){
            var menuid = $("select").val();
            if(menuid==null||menuid=='-1'){
                menuid='';
            }
            $(".msg").show();
            var page=$("#page").val();
            $.ajax({
                url:'/mgr/getTopics',
                data:{"menuid":menuid,"page":page},//获取全部
                type:"POST",
                dataType:'json', //预期服务器返回的数据类型
                error: function() {
                    alert("查询出错！");
                },
                success:function(data){
                    if(data.CODE=='0'){
                        var topics = data.TOPICS;
                        var str='';
                        var len = topics.length;
                        for(var i=0;i<len;i++){
                            var id=topics[i].id;
                            str+='<tr id="'+id+'">';
                            str+='<td>'+((i+1)+((page-1)*20))+'</td>';
                            str+='<td class="title"><div class="title_div">'+htmlEncode(topics[i].title)+'</div></td>';
                            /*str+='<td class="description"><div class="description_div">'+topics[i].description+'</div></td>';*/
                            var visible = topics[i].visible;
                            if(visible=='0'){
                                str+='<td class="display">草稿</td>';
                            }
                            if(visible=='1'){
                                str+='<td class="display">已发布</td>';
                            }
                            str+='<td  class="mobile">'+topics[i].create_datetime+'</td>';
                            str+='<td  class="mobile">'+topics[i].update_datetime+'</td>';
                            str+='<td  class="displayFunc">';
                            var menuid = topics[i].menuid;
                            if(visible=='0'){
                                str+='<button class="btntools displayFuncDo"  onclick="visibleTopic(\''+id+'\',\''+menuid+'\',1)">发布</button>';
                            }
                            if(visible=='1'){
                                str+='<button class="btntools displayFuncDo"  onclick="visibleTopic(\''+id+'\',\''+menuid+'\',0)">取消</button>';
                            }
                            str+='<button class="btntools" onclick="editTopic(\''+id+'\',\''+menuid+'\',\''+topics[i].type+'\')">编辑</button>';
                            str+='<button class="btntools" onclick="deleteTopic(\''+id+'\',\''+menuid+'\')">刪除</button>';
                            str+='</td>';
                            str+='</tr>';
                        }
                        //alert(str);
                        $("#topictable").append(str);
                        $(".msg").hide();
                        if(len==0){
                            $(".more").hide();
                        }else{
                            $(".more").show();
                        }

                    }else{
                        alert(data.info);
                    }
                }
            });
        }
        function deleteTopic(id,menuid){
            if(!window.confirm("删除后将不可恢复？")){
                return;
            }

            $.ajax({
                url:'/mgr/deleteTopic',
                data:{"menuid":menuid,"topicid":id},
                type:"POST",
                dataType:'json', //预期服务器返回的数据类型
                error: function() {
                    alert("刪除失敗！");
                },
                success:function(data){
                    if(data.CODE=='0'){
                        alert("操作成功");
                        $("#"+id).hide();
                    }else{
                        alert("操作失败");
                    }
                }
            });
        }
        function getTopicsBefore() {
            $("#page").val("1");
            $("#topictable").html("<tr><th>序号</th><th>标题</th><th>状态</th><th  class='mobile'>创建时间</th><th  class='mobile'>更新时间</th><th>操作</th></tr>");
            getTopics();
        }
        function editTopic(id,menuid,type){
            if(type=="1"){
                window.location.href="/mgr/topic/"+id+"/"+menuid;
            }else{
                window.location.href="/mgr/mobileTopic/"+id+"/"+menuid;
            }
        }
        function visibleTopic(id,menuid,flag){
            $.ajax({
                url:'/mgr/visibleTopic',
                data:{"menuid":menuid,"topicid":id,"visible":flag},
                type:"POST",
                dataType:'json', //预期服务器返回的数据类型
                error: function() {
                    alert("操作失敗！");
                },
                success:function(data){
                    if(data.CODE=='0'){
                        alert("操作成功");
                        var str="";
                        var msg="";
                        var newflag =1;
                        if(flag==0){
                            str="草稿";
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
                        $("#"+id+" .displayFunc .displayFuncDo").attr("onclick","visibleTopic(\'"+id+"\',\'"+menuid+"\',"+newflag+")");
                    }else{
                        alert("操作失败");
                    }
                }
            });
        }
    </script>
</head>
<body>
<input type="hidden" id="page" value="1">
<div id="topics">
<div id="top">
    <div id="top1">
    <select>
    </select>
    <button onclick="getTopicsBefore()">查询</button>
    </div>
    <div id="top2">
        <a href="/mgr/topic/0/0"><button>新建笔记</button></a>
    </div>
</div>


<table id="topictable" border="1px">
    <tr><th>序号</th><th>标题</th><!--<th>内容</th>--><th>状态</th><th  class='mobile'>创建时间</th><th  class='mobile'>更新时间</th><th>操作</th></tr>
</table>
<div class="msg" style="display: none;">正在查询中...</div>
<div class="more" onclick="more()"><button>点击加载更多</button></div>
</div>
</body>
</html>