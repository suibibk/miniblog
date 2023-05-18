/****************************************************common js start by lxm*************************************************************************/
function CommonJs(){
	var dateFormat = "";
	
}

CommonJs.formatDateAndTime = function(data){
	if(data.length==6){
		/*到分*/
		dateFormat = data.substring(0,2)+":"+data.substring(2,4);
	}else if(data.length==8){
		dateFormat = data.substring(0,4)+"-"+data.substring(4,6)+"-"+data.substring(6,8);
	}
	return dateFormat; 
};  

/****************************************************common js end****************************************************************************/

//----------------------------------------begin---------------------------------------------------
/**20161205
 *by liangzhaohui 
 *
 * 判断18位身份证是否合法
 *公民身份证号码是特征组合码，由17位数字本体码和一位数字校验码组成
 *排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码
 *顺序码：表示在同一地址码所标识的区域范围内，对同年同月同日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配给女性
 *1.前1、2位数字表示：所在省份的的代码；2.第3、4位数字表示：所在城市的代码；3.第5、6位数字表示：所在区县的代码；
 *4.第7~14位数字表示：出生年、月、日；5.第15、16位数字表示：所在地的派出所的代码；
 *6.第17位数字表示性别：奇数表示男性，偶数表示女性；
 *7.第18位是校验码；也有人说是个人信息码，一般是随机计算产生，用来检验身份证的正确性。校验码可以是0~9的数字，有时也用x表示。
 *
 *第18位数字（校验码）的计算方法为：
 1.将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别7、9、10、5、8、4、2、1、6、3、7、9、10、5、8、4、2
 2.将这17位数字和系数相乘的结果相加。
 3.用加出来的和除以11，看余数是多少。
 4.余数只可能有0、1、2、3、4、5、6、7、8、9、10这11个数字。其分别对应的最后一位身份证的号码为1、0、X、9、8、7、6、5、4、3、2
 */
//验证值是否为身份证
function isIdCard(value)
{
    if (value.length == 0)
    {
        return false;
    }
    var reg = /^\d{17}([Xx0-9])$/;
    if (!reg.test(value))
    {
      // alert("身份证号码格式错误");
        return false;
    }else{
        if(!idcard_checksum18(value)){//校验错误
            //alert("身份证号校验有误，请核对填写的身份证号码");
            return false;
        }
    
    }
    return true;
}

//18位身份证校验码有效性检查
function idcard_checksum18(idcard) {
    if (idcard.length != 18) {
        return false;
    }
    var idcard_base = idcard.substr(0, 17);
    if (idcard_verify_number(idcard_base) != idcard.substr(17, 1).toUpperCase()) {
        return false;
    } else {
        return true;
    }
}

// 计算身份证校验码，根据国家标准GB 11643-1999
function idcard_verify_number(idcard_base) {
    if (idcard_base.length != 17) {
        return false;
    }
//加权因子
    var factor = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2);
//校验码对应值
    var verify_number_list = new Array('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2');
    var checksum = 0;
    for (var i = 0; i < idcard_base.length; i++) {
        checksum += idcard_base.substr(i, 1) * factor[i];
    }
    var mod = checksum % 11;
    var verify_number = verify_number_list[mod];
    return verify_number;
}

//---------------------------------end----------------------------------------------------------------------

/****公共的js操作方法20170620 lwh****/
//校验手机号是否有效
//验证手机号码的有效性
function validatePhone(mobile){
	//验证手机号码的有效性
	if(mobile.length==0) 
    { 
       //alertMsg('请输入手机号码！'); 
       return false; 
    }     
    if(mobile.length!=11) 
    { 
        //alertMsg('请输入有效的手机号码！'); 
        return false; 
    } 
     
    var myreg = /^(((13[0-9]{1})|(14[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\d{8})$/;  
    if(!myreg.test(mobile)) 
    { 
        //alertMsg('请输入有效的手机号码！'); 
        return false; 
    } 
	return true;
}

/**
 * 根据时间格式yyyyMMddHHmmss和flag类型返回预订的时间格式
 * flag为0 返回格式yyyy年MM月dd日 HH时mm分ss秒
 * flag为1 返回格式yyyy-MM-dd HH:mm:ss
 * flag为2 返回格式yyyy年MM月dd日
 */
function getFormatTime(time,flag){
	var time1='';
	var time2='';
	if(flag==0){
		time1 = time.substring(0,4)+'年'+time.substring(4,6)+'月'+time.substring(6,8)+'日 ';
		time2 = time.substring(8,10)+'时'+time.substring(10,12)+'分'+a.substring(12,14)+'秒';
	}
	if(flag==1){
		time1 = time.substring(0,4)+'-'+time.substring(4,6)+'-'+time.substring(6,8)+' ';
		time2 = time.substring(8,10)+':'+time.substring(10,12)+':'+time.substring(12,14);
	}
	if(flag==2){
		time1 = time.substring(0,4)+'年'+time.substring(4,6)+'月'+time.substring(6,8)+'日 ';
		time2 = '';
	}
	return time1+time2;
}

/**
 * 根据字符串日期格式，获取对应的毫秒值，只支持两种
 * yyyyMMdd  和  yyyyMMddHHmmss
 * @param time
 * @returns
 */
function getLong(time){
	var len = time.length;
	var str=time.substring(0,4)+"/"+time.substring(4,6)+"/"+time.substring(6,8);
	var t1 = new Date(Date.parse(str)).getTime();
	if(len==14){
		var hh = parseInt(time.substring(8,10));
		var mm = parseInt(time.substring(10,12));
		var ss = parseInt(time.substring(12,14));
		var t2 = (hh*60*60+mm*60+ss)*1000
		t1=t1+t2;
	}
	return t1;
}
/**
 * 两个数相乘（包括小数）
 * @param arg1
 * @param arg2
 * @returns {Number}
 */
function accMul(arg1,arg2)
{
var m=0,s1=arg1.toString(),s2=arg2.toString();
try{m+=s1.split(".")[1].length}catch(e){}
try{m+=s2.split(".")[1].length}catch(e){}
return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)
}
/**
 * 两个数想除 包括小数
 */
function accDiv(arg1,arg2){
	var t1=0,t2=0,r1,r2;
	try{t1=arg1.toString().split(".")[1].length}catch(e){}
	try{t2=arg2.toString().split(".")[1].length}catch(e){}
	with(Math){
	r1=Number(arg1.toString().replace(".",""))
	r2=Number(arg2.toString().replace(".",""))
	return (r1/r2)*pow(10,t2-t1);
	}
}

/**************JS输出文本（不识别JS） lwh 20180110******************/
function htmlEncode(html){
	
	//alert(html);
	  //1.首先动态创建一个容器标签元素，如DIV
  var temp = document.createElement ("div");
  //2.然后将要转换的字符串设置为这个元素的innerText(ie支持)或者textContent(火狐，google支持)
  (temp.textContent != undefined ) ? (temp.textContent = html) : (temp.innerText = html);
  //3.最后返回这个元素的innerHTML，即得到经过HTML编码转换的字符串了
  var output = temp.innerHTML;
  temp = null;
  return output;
}
/**************JS过滤特殊字符，有的话返回true lwh 20180110******************/
function  cleanXSS(value) {
  //You'll need to remove the spaces from the html entities below
 var re = new RegExp("['?;\"<>|~]","gm");
 return re.test(value);

}

/**
 * 计算字符串实际长度（一个汉字长度为2，普通字符为 1）
 * add by huanggl 20180110 
 */

function getByteLen(val) {
	    var len = 0;
	    for (var i = 0; i < val.length; i++) {
	        var a = val.charAt(i);
	        if (a.match(/[^\x00-\xff]/ig) != null) {
	            len += 2;
	        }
	        else {
	            len += 1;
	        }
	    }
	    return len;
}

/*
 *使用“\”对特殊字符进行转义，除数字字母之外，小于127使用16进制“\xHH”的方式进行编码
 *add by huanggl 20180110
 */
var JavaScriptEncode = function(str){
    
    var hex=new Array('0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f');
        
    function changeTo16Hex(charCode){
        return "\\x" + charCode.charCodeAt(0).toString(16);
    }
    
    function encodeCharx(original) {
        
        var found = true;
        var thecharchar = original.charAt(0);
        var thechar = original.charCodeAt(0);
        switch(thecharchar) {
            case '\n': return "\\n"; break; //newline
            case '\r': return "\\r"; break; //Carriage return
            case '\'': return "\\'"; break;
            case '"': return "\\\""; break;
            case '\&': return "\\&"; break;
            case '\\': return "\\\\"; break;
            case '\t': return "\\t"; break;
            case '\b': return "\\b"; break;
            case '\f': return "\\f"; break;
            case '/': return "\\x2F"; break;
            case '<': return "\\x3C"; break;
            case '>': return "\\x3E"; break;
            default:
                found=false;
                break;
        }
        if(!found){
            if(thechar > 47 && thechar < 58){ //数字
                return original;
            }
            
            if(thechar > 64 && thechar < 91){ //大写字母
                return original;
            }

            if(thechar > 96 && thechar < 123){ //小写字母
                return original;
            }        
            
            if(thechar < 127){ //其他字符
              return changeTo16Hex(original);
            }
            /*
            //大于127用unicode
            if(thechar>127) { 
                var c = thechar;
                var a4 = c%16;
                c = Math.floor(c/16); 
                var a3 = c%16;
                c = Math.floor(c/16);
                var a2 = c%16;
                c = Math.floor(c/16);
                var a1 = c%16;
                return "\\u"+hex[a1]+hex[a2]+hex[a3]+hex[a4]+"";        
            }
            else {
                return changeTo16Hex(original);
            }
            */
        }
    }     
  
    var preescape = str;
    var escaped = "";
    var i=0;
    for(i=0; i < preescape.length; i++){
        escaped = escaped + encodeCharx(preescape.charAt(i));
    }
    return escaped;
}
/**
 * 20180614 lwh  比较两个日期相差的年月日
 * @param startTime yyyyMMdd
 * @param endTime yyyyMMdd
 */
/*function getYearMonthDayDiff(startTime,endTime){
	//var sDate = new Date(Date.parse(startTime.replace(/-/g, "/")));
    //var eDate = new Date(Date.parse(endTime.replace(/-/g, "/")));
	var sDate = new Date(Date.parse(startTime));
    var eDate = new Date(Date.parse(endTime));
	var sY  = sDate.getFullYear();     
    var sM  = sDate.getMonth();
    var sD  = sDate.getDate();//反正是算差值，不需要相减
    var eY  = eDate.getFullYear();
    var eM  = eDate.getMonth();//反正是算差值，不需要相减
    var eD  = eDate.getDate();
	var year = eY-sY;
	var month = eM-sM;
	var day = eD-sD;
	console.log("year:"+year+"month:"+month+"day:"+day);
	if(day<0){
		month--;
		eDate.setMonth(eDate.getMonth());//这里用原来的就表明减一了
		eDate.setDate(0);//这样就可以获取月份的日期
		day = day+eDate.getDate();
	}
	if(month<0){
		month = (month +12) %12;
	    year--;	
	}
	console.log("year:"+year+"month:"+month+"day:"+day);
	return {year,month,day};
}*/
//校验邮箱格式
/*function emailCheck(obj) {
	 if (obj.length == 0)
	    {
	        return true;
	    }
    var pattern = /^([\.a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/;  
    if (!pattern.test(obj)) {  
        alert("请输入正确的邮箱地址。");  
        return false;  
    }  
    return true;
}*/

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
            /*str = str.substring(0,str.indexOf(matchImg[0]));
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
    //这里要去掉很多漏网之鱼,就只最后的结尾<p,<b,<u,<s
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