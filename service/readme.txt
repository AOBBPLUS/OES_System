#service模块
本模块包括了在线教育系统后端的各类服务

***
##service-acl模块
本模块包括了权限管理api接口，包含了权限分配，用户管理查询，菜单构建等功能

***
##service-cms模块
本模块包括了广告管理功能，定义了CrmBanner类，可在CrmBannerAdminController.java中后台管理，CrmBannerFrontControll.java中前台管理

***
##service-edu模块
本模块包括了教学相关功能
###client包
client包下包括了Order、Ucenter、Vod客户端及各客户端熔断时使用的服务类
###controller包
controller包下包括了各界面的控制器

课程章节、课程信息、课程简介、课程科目、课程小节视频管理控制器

####front
包括了课程评论、课程前台显示、前端门户页面显示、讲师前台显示管理控制器

###entity包
包括了各类实体

###listener包
包括了科目的服务类

###Mapper包
包括了各类mapper接口

***
##service-order模块
本模块包括了订单的各项服务

###client包
包括了Course和Ucenter客户端及其熔断回调

###controller包
包括了用户购买订单，支付日志表管理控制器

###entity包
定义了购买订单和支付日志表实体

###mapper包
包括了各类mapper接口

###service包
包括了订单和日志服务及其实现类

###utils包
包括了oss配置类，用于读取application.properties；http请求工具类；随机生产订单号功能

***
##service-oss模块
本模块包括了阿里云对象存储OSS服务管理控制器、服务及其实现类，配置类。

***
##service-sms模块
本模块包括了腾讯云短信发送服务,腾讯云免费额度为200条

***
##service-statistics模块
本模块包括了数据统计功能

提供了用户中心客户端及其熔断回调，数据统计管理控制器、mapper接口，定时任务类，服务和实现类

***
##service-ucenter模块
本模块包括了用户中心功能

提供学员用户登录、学院微信登录管理控制器，会员类，登陆注册功能，mapper接口，用户中心服务及实现类

***
##service-vod模块
本模块包括了阿里云视频功能