<div id="first">
		<img id="tools" onclick="see_more()" src="/image/tools.png">
		<div id="cover" onclick="close_see_more();"></div>
        <div id="first_left">
            <span onclick="window.location.href='/'">&nbsp;${setUp.blog_name!''}</span>
        </div>
        <div id="first_right">
         	<div class="search">
				<form target="_self" action="/search" onsubmit="if(value.value.length<2){alert('温馨提示：关键字最少为2');return false;}">
					<input type="search" class="text" name="value" placeholder="请输入关键字" value="${value!''}">
					<input type="submit" class="btn" value="">
				</form>
			</div>
        </div>
</div>