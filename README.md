# unmz-common-util
常用的Java工具类

[![license](https://img.shields.io/github/license/FaritorKang/unmz-common-util.svg)](https://opensource.org/licenses/MIT)
[![maven](https://img.shields.io/maven-central/v/net.unmz.java/unmz-common-util.svg)](https://search.maven.org/artifact/net.unmz.java/unmz-common-util)


**本工具中的方法有部分采自网络，用与归纳整理，如有侵权，请联系我**

**1.0.24版本开始不再兼容Java1.7**

项目现已发布依赖地址:

    <dependency>
        <groupId>net.unmz.java</groupId>
        <artifactId>unmz-common-util</artifactId>
        <version>1.0.25</version>
    </dependency>

现已包含:

    1.Http请求与响应
    2.Json处理
    3.SMS短信发送接口
    4.图片和Base64互转
    5.Map工具类
    6.MD5工具类
    7.Sign签名工具类
    8.Xml工具类
    9.网络IP获取工具类
    10.图形验证码工具类
    11.Unicode转换
    12.自定义数字/字符串验证码生成
    13.数组工具类
    14.Express快递第三方接口实现
    15.AES/RSA工具类
    16.UUID工具类
    17.对象属性可否为空,长度等校验
    18.二维码生产工具类

#### 更新日志:

##### 2021-07-15 v1.0.25

    [M]升级xstream版本
    [M]升级httpclient版本
    [M]升级fastjson版本
    [M]调整二维码工具类


##### 2021-03-05 v1.0.24

    [A]新增二维码工具类
    [M]升级fastjson版本

##### 2020-08-19 v1.0.23

    [M]升级fastjson版本

##### 2020-06-25 v1.0.22

    [M]完善随机字符串工具类
    [M]升级fastjson版本

##### 2020-04-13 v1.0.21

    移除JsonUtils工具类static静态代码块中的时间配置

##### 2020-03-27 v1.0.20
    
    新增SHA1工具类
    新增类工具类通过反射动态添加对象的属性与值

##### 2020-03-16 v1.0.19

    更新AES工具类

##### 2020-02-22 v1.0.18

    更新XmlUtils兼容旧版本toString方法

##### 2020-02-21 v1.0.17

    更新XmlUtil的toBean和toString方法,新增toJsonString

##### 2019-09-16 v1.0.16

    升级fastjson依赖包

##### 2019-06-28 v1.0.15

    优化调整AES工具类
    升级fastjson工具类[FastJSON远程代码执行0day漏洞](https://helpcdn.aliyun.com/document_detail/123431.html)


##### 2018-12-19 v1.0.14

    自定义校验接口扩展为可用于参数列表中校验
    更新AES工具类
    

##### 2018-10-15 v1.0.13

    新增自定义注解,校验入参中属性的合法性


##### 2018-09-30 v1.0.12

    1.修复XmlUtils工具类可以会导致XXE的问题
    2.修复DateUtils计算两天之间的差值精度问题

##### 2018-08-06 v1.0.11

    1.修复httpUtil部分请求时参数异常的问题
    2.新增UUID工具类


##### 2018-07-05 v1.0.10
    
    新增delete方式请求时，可加参数
        
##### 2018-06-09 v1.0.9
    
    更新SignUtils工具类，新增验签方法

##### 2018-06-02 v1.0.8

    1.新增AES/RSA工具类
    2.更新SignUtils工具类
    
##### 2018-05-30 v1.0.7
    
    更新时间工具类
    
##### 2018-01-23 v1.0.0-1.0.6

    已经记不清楚了...
    
    
联系:
faritor#unmz.net