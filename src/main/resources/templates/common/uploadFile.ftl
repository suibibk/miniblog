<style>
.uploadFileAssembly {
    position:relative;
    height:40px;
    width:40px;
    display: inline-block;
    background: #F5F5F5;
    border-radius: 4px;
    overflow: hidden;
    text-decoration: none;
    text-indent: 0;
    background:url("/image/default.jpg") no-repeat;
    background-size: 40px 40px;
    vertical-align: middle;
    padding: 0;
    margin: 0;
}
.uploadFileAssembly input {
    width:100%;
    position: absolute;
    font-size: 100px;
    right: 0;
    top: 0;
    opacity: 0;
    height:100%;
}
</style>
<script>
	var uploadone=true; 
    function uploadFile${id}() {
    	if(!uploadone){
    		return;
    	}
    	uploadone=false; 
        var formData = new FormData($("#${id}_span form")[0]);
        
        var ing="/image/timg.gif";
        $("#${id}_span").css("background-image","url("+ing+")");
        $.ajax({
            url: '/mgr/uploadImg' ,
            type: 'POST',
            data: formData,
            async: true,
            cache: false,
            contentType: false,
            processData: false,
            success: function (data) {
            	if(data.success=="1"){
                  	var url = data.url;
                    $("#${id}").val(url);
                    if(url.indexOf("musics")!=-1){
                    	url="/image/music.jpg"
                    }
                    $("#${id}_span").css("background-image","url("+url+")");
                  }
                  uploadone=true; 
            },
            error: function () {
                 alert("上传失败");
	                 var ing2="/image/shangchuan.jpg";
	                 $("#${id}_span").css("background-image","url("+ing2+")");
	                 uploadone=true; 
                  }
             });
    }
    $(function(){
        $("#${id}_span form input").change(function () {
          //如果value不为空，调用文件加载方法
          if($(this).val() != ""){
               uploadFile${id}();
          }
     });
    });
</script>
<span id="${id}_span" class="uploadFileAssembly">
    <form><input type="file" class="uploadFileAssemblyInput" name="editormd-image-file" placeholder="输入文件" /></form>
    <input type="hidden" value="" id="${id}" class="uploadFileAssemblyId">
</span>