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
    <title>博文详情</title>
    <link rel="stylesheet" href="/editormd/css/editormd.min.css" />
    <script src="/js/jquery.min.js"></script>
    <script src="/editormd/js/editormd.min.js"></script>
    <style>
          body{
        margin: 8px;
	        color: #333;
	    font-size: 16px;
	    }

        #topic{
            width:95%;
            margin: 0px auto;
        }
        #top{
            text-align: left;
            height: 85px;
            margin-bottom: 10px;
        }
        #top1{
            width:100%;
            margin-bottom: 15px;
            line-height: 30px;
            color: #4c4b4b;
        }
        #top2{
            width:100%;
            height:40px;
            margin-bottom: 1px;
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
        #title{
            width:100%;
            height:100%;
            font-size: 16px;
            text-indent: 2px;
            color: #4c4b4b;
        }
        #menus{
            height:100%;
            float: left;
            color: #4c4b4b;
        }
        #etittools{
            color: #4c4b4b;
        }
        #save{
            height: 100%;
            float: right;
            text-align: right;
            color: #551A8B;
            line-height: 30px;
        }
        #myblog2{
            font-size: 16px;
            height: 60px;
            width: 100%;
            text-align: center;
            display: none;
            margin-top: 5px;
        }
        #e{
            font-size: 13px;
            color: #333;
        }
        .editormd-html-preview blockquote, .editormd-preview-container blockquote {
		    color: #666;
		    border-left: 6px solid #e6e6e6;
		    padding-left: 20px;
		    margin-left: 0;
		    font-size: 14px;
		    font-style: normal;
		    background-color: #fafafa;
		    padding: 10px;
		}
    </style>
    <script type="text/javascript">
        $(function () {
            var topicid=$("#topicid").val();
            if(topicid=="0"){
                $("#myblog2").show();
            }
            
            var topicurl="${topicurl}"
            if(topicurl!=""){
            	 $("#topicurl").val(topicurl);
             	$("#topicurl_span").css("background-image","url("+topicurl+")");
            }
            
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
                       // var str='<option value="-1">选择所属标签</option>';
                        var str='';
                        var menuid = $("#menuid").val();
                        for(var i=0;i<menus.length;i++){
                            if(menus[i].type=='1') {
                                if(i==0){
                                    str+='<option value="' + menus[i].id + '" selected>' + menus[i].name + '</option>';
                                }else{
                                    str+='<option value="' + menus[i].id + '">' + menus[i].name + '</option>';
                                }

                            }
                        }
                        //alert(str);
                        $("#menus").html(str);
                        if(menuid!="0"){
                            $("select").val(menuid);
                        }
                    }else{
                        alert("失败");
                    }
                }
            });
        }
        $(function() {
            getMenus();
            editormd("my-editormd", {//注意1：这里的就是上面的DIV的id属性值
                width   : "100%",
                height  : 500,
                syncScrolling : "single",
                path    : "/editormd/lib/",//注意2：你的路径
                saveHTMLToTextarea : true,//注意3：这个配置，方便post提交表单
                emoji	: true,
                
                codeFold : true,
                htmlDecode : true,
                
                taskList : true,
                tocm : true,                  // Using [TOCM]
                tex : true,                   // 开启科学公式TeX语言支持，默认关闭
                flowChart : true,             // 开启流程图支持，默认关闭
                sequenceDiagram : true,       // 开启时序/序列图支持，默认关闭,
                /**上传图片相关配置如下*/
                imageUpload : true,
                imageFormats : ["jpg", "jpeg", "gif", "png", "bmp", "webp","JPG", "JPEG", "GIF", "PNG", "BMP", "WEBP"],
                imageUploadURL : "/mgr/uploadImg",//注意你后端的上传图片服务地址
                /**期望返回格式
                 {
    				success : 0 | 1, //0表示上传失败;1表示上传成功
   					message : "提示的信息",
    				url     : "图片地址" //上传成功时才返回
				}
                 **/
            });
           // $("#my-editormd-markdown-doc").val("123214324");
           
            //1分钟存1次草稿
	    	setInterval(saveCurrentTopic, 1000*60); //2分钟存1次草稿
        });
        
        var end=false;
        function saveCurrentTopic(){
        	//alert("存草稿成功");
        	if(!end){
        		saveTopic(0);
        	}
        }
        
        
        var one = true;
        function saveTopic(visible){
        	if(visible==1){
        		 if(!window.confirm("确定要发布？")){
		                return;
		            }
        	}
           
            if(!one){
                return;
            }
            one=false;
            if(visible==1){
            	end=true;
            }
            var menuid=$("#menus").val();
            if(menuid=="0"){
                alert("请选择所属标签");
                one = true;
                return;
            }
            var title=$("#title").val();
            if(title==""){
            	if(visible==1){
                	alert("标题不能为空");
                }
                one = true;
                return;
            }
            var content=$("#my-editormd-markdown-doc").val();
            if(content==""){
            if(visible==1){
                alert("博文不能为空");
                }
                one = true;
                return;
            }
            var topicid=$("#topicid").val();
            if(topicid=="0"){
            	topicid="";
            }
            var topicurl=$("#topicurl").val();
            var description = getDescription();
            if(description==""){
                description="查看详情";
            }
            var viewcontent=$(".editormd-preview-container").html();
         //   if(topicurl==""){
             	//	topicurl=imgUrlFun(viewcontent);
             		//if(topicurl==""){
             			//topicurl="/image/suolue.png";
             		//}
           //  }
           
           var historyId = $("#historyId").val();
            $.ajax({
                url: "/mgr/saveTopic",
                type: "POST",
                dataType:'json',
                data : {"historyId":historyId,"menuid":menuid,"topicid":topicid,"title":title,"type":"1","content":content,"description":description,"viewcontent":viewcontent,"topicurl":topicurl,"visible":visible},//获取所有的菜单
                success: function (data) {
	                var code=data.CODE;
	                if(code!="0"){
	                	if(code=="2"){
	                		alert("默认只能发布10条博文，请联系QQ(1196216842)获取校验码");
	                	}else{
		                	alert("执行失败,请重试");
	                	}
	                	one = true;
	                	return;
	                }else{
	               		 one = true;
	                	if(visible==1){
		                	 alert("成功");
		                	 window.location.href="/mgr/topic/0/0";
		                }else{
		                	$("#topicid").val(data.obj);
		                }
	                   
	                }
                },
                error : function (data) {
                    alert("添加失败");
                    one = true;
                }
            });
        }
		function imgUrlFun(str){
			var data = '';
		    str.replace(/<img [^>]*src=['"]([^'"]+)[^>]*>/, function (match, capture) {
		          data =  capture;
		    });
		    return data;
		}
        function getDescription(){
            //获取列表的提示截取第一段,存放200个字符
            var value=$(".editormd-preview-container").html();
            var length = value.length;
            //这里可以取前面两千个，后台截取
            if(length<=2000){
                return value;
            }
            //若是大于200，则截取前面200
            var desc = value.substring(0,2000);
            //return closeHTML(desc)
            return desc;
        }
        function closeHTML(str){
            var arrTags=["pre","a","code","span","font","h1","h2","h3","h4","h5","h6",,"li","ul","table","div","span"];
            //匹配图片
            var img = "img";
            var re=new RegExp("\\<"+img+"","ig");
            var matchImg=str.match(re);
            if(matchImg!=null){
                var reg = new RegExp("(<img *src=\"\.*?\>)","ig");
                var images=str.match(reg);
                if(images!=null){
                    return images[0];
                   /* str = str.substring(0,str.indexOf(matchImg[0]));
                    return str;*/
                }

            }
            for(var i=0;i<arrTags.length;i++){
                var re=new RegExp("\\<"+arrTags[i]+"","ig");
                var arrMatch=str.match(re);
                var open = 0;
                var end = 0;
                if(arrMatch!=null){
                    //alert(arrMatch);
                    open= arrMatch.length;
                    //这里要去找到对应的闭区间，否则就直接截取
                    re=new RegExp("\\<\\/"+arrTags[i]+"\\>","ig");
                    var arrMatch2=str.match(re);
                    if(arrMatch2!=null){
                        //alert(arrMatch2);
                        end = arrMatch2.length;
                    }
                    if(open!=end){
                        str = str.substring(0,str.indexOf(arrMatch[0]))
                        return str;
                    }
                }

            }
            str=str.trim();
            var last1 = str.substring(str.length-1,str.length);
            if(last1=="<"){
                str=str.substring(0,str.length-1);
            }
            var last2 = str.substring(str.length-2,str.length);
            if(last2=="<p"||last2=="<b"||last2=="<s"||last2=="<u"){
                str=str.substring(0,str.length-2);
            }
            //先解决三位的
            var last3= str.substring(str.length-3,str.length);
            if(last3=="<pr"||last3=="<br"){
                str=str.substring(0,str.length-3);
            }
            //先解决三位的
            var last4= str.substring(str.length-4,str.length);
            if(last4=="<spa"||last4=="<cod"){
                str=str.substring(0,str.length-4);
            }
            return str;
        }
        function changeEdit() {
            window.location.href="/mgr/mobileTopic/0/0";
        }
    </script>
</head>
<body>
<div id="myblog2">
    切换编辑器：<select id="etittools">
        <option value="1" selected>markdown</option>
        <option value="2" >Editor</option>
    </select>
    <div id="e">推荐使用markdown编辑器&nbsp;<span style="color: #9e3e46;"></span></div>
</div>

<div id="topic">
    <div id="top">
        <div id="top1">
            <select id="menus">
            </select>
            <div id="save" onclick="saveTopic(1)"><button>发布</button></div>
        </div>
        <div id="top2">
          <input type="text" id="title" maxlength="200" value ="${title}" placeholder="请输入博文标题">
        </div>
    </div>
    <!--为了兼容以前的文档，这里要有一个历史ID，若是历史ID存在，则新建文档以历史ID为准-->
    <div style="margin-bottom:5px;">封面图：<#assign id = "topicurl"><#include "/common/uploadFile.ftl">&nbsp;<input type="text"  style="display: none" id="historyId" value="" placeholder="请入输入历史ID" ></div>
    <div id="my-editormd" >
        <textarea id="my-editormd-markdown-doc" name="my-editormd-markdown-doc" style="display:none;">${content}</textarea>
        <!-- 注意：name属性的值,经测试内容是一样的-->
        <textarea id="my-editormd-html-code" name="my-editormd-html-code" style="display:none;"></textarea>
    </div>
</div>
<input type="hidden" id="topicid" value="${topicid}">
<input type="hidden" id="menuid" value="${menuid}">
<script>
    $("#etittools").change(function () {
        var value = $(this).val();
        if(value=="1"){
            window.location.href="/mgr/topic/0/0";
        }
        if(value=="2"){
            window.location.href="/mgr/mobileTopic/0/0";
        }
    });
</script>
</body>
</html>