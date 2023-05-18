<style>
	.bottom{
	    text-align: center;
	    font-size: 12px;
	    color: #777;
	    margin-bottom: 30px;
	    margin-top: 10px;
	    
	}
	.bottom div{
	    margin-bottom: 5px;
	}
	.bottom td img{
		border:1px solid #777;
	}
.scroll {
    width: 40px;
    height: 40px;
    position: fixed;
    right: 15px;
    bottom: 40px;
    z-index:999;
}
 
.scroll:hover {
    opacity: 0.8;
    cusor:pointer;
}
#baxx{
	cursor: pointer;
}
#baxx:hover{
	color:#3fa1a7;
}


</style> 
<img src="/image/top.png" title="回到顶部" class="scroll"> 
<div class="bottom">
       <div>Copyright&nbsp;:&nbsp;${setUp.copyright!''} &nbsp;&nbsp;<span id="baxx" onclick="togxb();">备案号&nbsp;:&nbsp;${setUp.beian_num!''}</span></div>
</div>
<script>
function  togxb() {
     window.location.href='https://beian.miit.gov.cn';
}
$(function () {
  $(".scroll").click(function (event) {
       scrollTo(0,0);
  });
  addVisit();
});

//添加页面访问记录
function addVisit(){
 	  var topicId = $("#topicId").val();
 	  var type = "01";
 	  if(topicId == undefined){
 	  		topicId="0";
 	  		type="02";
 	  }
       $.ajax({
            url:'/addVisit',
            data:{"id":topicId,"type":type},
            type:"POST",
            dataType:'json', //预期服务器返回的数据类型
            error: function() {
            },
            success:function(data){
                   
            }
          });
 }
</script>
