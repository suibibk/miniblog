<link rel="stylesheet" href="/ztree/zTreeStyle.css?v=1" type="text/css">
<script type="text/javascript" src="/ztree/jquery.ztree.core.js?v=9"></script>
	<div class="menu">
        <div class="m_title"><span>网站介绍</span></div>
        <div class="menu_div"><img src="/image/user.png">&nbsp;<span class="menu_desc">${setUp.name!''}</span></div>
        <div class="menu_div"><img src="/image/qq.png">&nbsp;<span class="menu_desc">${setUp.qq!''}</span></div>
        <div class="menu_div"><img src="/image/email.png">&nbsp;<span class="menu_desc">${setUp.email!''}</span></div>
        <div class="menu_div"><img src="/image/desc.png">&nbsp;<span class="menu_desc">${setUp.desc!''}</span></div>
    </div>
    <div class="menu">
        <div class="m_title"><span>网站统计</span></div>
        <div  class="menu_div">访问&nbsp;${PAGE_VISIT}</div>
    </div>
    <div class="menu">
        <div class="m_title"><span>天天背单词学英语(微信)</span></div>
        <img style="width: 80%;" src="https://www.suibibk.com/fileupload/images/202305/1684327515222.jpg">
    </div>
    <div class="menu">
     	<div class="m_title"><span>网站标签</span></div>
		<div class="ztree" id="treeDemo"></div>
	</div>
	
	
	<script>
	var strs=new Array();
	var first={"id":0, "pId":0,"name":"分类", "open":true} ;
	strs.push(first);
	<#if menus??>
         <#list menus as menu>
         	var id = '${menu.id}';
            var pId =  '${menu.parent_id}';
            var name = '${menu.name}';
            var type ='${menu.type}';
            var url = "/menu/"+id;
            if(type=="2"){
                url='${menu.link!''}';
            }
            var jsonStr ={"id":id, "pId":pId, "name":name, "url":url,"target":"_self"} ;
            if(type=="0"){
                 jsonStr ={"id":id, "pId":pId, "name":name, "open":true} ;
            }
            strs.push(jsonStr);
        </#list>
 	</#if>
 	var setting = {
								data: {
									simpleData: {
										enable: true
									}
								}
							};
						$.fn.zTree.init($("#treeDemo"), setting, strs);
						$("#treeDemo").show();
 	
 	
 	function close_see_more(){
         $("#right").animate({left:'-290px'},400,"linear");	
         $("#tools").attr("onclick","see_more()");
         $("#cover").hide();
    }
    function see_more(){
         $("#right").animate({left:'0px'},400,"linear",function(){});
         $("#tools").attr("onclick","close_see_more()")	;
         $("#cover").show();
	}
    </script>
