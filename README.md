### 一、迷你博客模板介绍
迷你博客是一个安装简单，使用简单，持续升级的轻量级博客模板，提供简便的博文发布功能，安装简单不用下载数据库。


### 二、云服务器下载
如果要安装在linux，可购买云服务器部署，当然如果有自己的服务器，放上去重启即可。[阿里云服务器购买](https://www.aliyun.com/minisite/goods?userCode=795vbv7z "阿里云服务器购买")


### 三、window服务器安装
先安装jdk1.8的环境，安装流程可以参考：[Window环境变量配置](https://www.suibibk.com/topic/599221164014829568 "Window环境变量配置") 也可以自行网上搜索。
解压程序，双击执行如下脚本即可
```
miniblog.bat
```

然后本地浏览器输入 http://localhost/ 即可访问

### 四、Linux服务器安装
#### 1、将迷你博客包上传到服务器目录
可以上传到任意目录，这里建议放到
```
/usr/local/
```
#### 2、解压
```
unzip miniblog.zip
```
这里若没有zip命令，需要先进行安装
```
yum install zip
yum install unzip
```

#### 3、安装jdk环境
```
yum install -y java-1.8.0-openjdk.x86_64
```
#### 4、开启80端口
```
systemctl start firewalld
firewall-cmd --zone=public --add-port=80/tcp --permanent
firewall-cmd --reload
```
#### 5、启动程序
```
cd miniblog
chmod 777 miniblog.sh
./miniblog.sh
```

以后重启只需要执行如下命令即可
```
./miniblog.sh
```

### 五、使用

#### 1、登录
点击网站标签的后台管理
默认密码为suibibk,登陆后麻烦再设置修改密码

#### 2、修改网站设置
这些设置具体对应首页显示的位置,其中备案号是在备案完后会提供，如果网站不备案，除非服务器是香港或者国外，或者直接用IP访问。

#### 3、修改网站密码
默认初始密码是suibibk，登录后台后需要重新设置网站密码

#### 4、基本使用
- 博文发布对应的是新建按钮，可以新建markdown或者普通格式的博文，代码记得按markdown的模式包含起来。
- 菜单对应的是博文右边的网站标签，可以包括目录，标签和外链，目前只支持二级。
- 首图对应的是公告和页码首页的首图。
- 笔记对应的是博文列表。