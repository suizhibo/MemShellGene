# MemShellGene

## 运行环境
已在下列JDK版本进行测试：

1. JDK 1.6.0_45
2. JDK 1.8.0_101
3. JDK 1.8.0_181
4. JDK 1.8.0_271
5. OpenJDK 11

## 启动
java -jar MemShellGene.jar

## 简介
### Attack模块

![attack](https://github.com/suizhibo/MemShellGene/assets/28916595/6021410e-6020-47a7-84b2-ea63442b55f4)





该模式下提供如下15个EXP
| EXP |
|--------|
|Shiro_550|
|Weblogic_CVE_2020_14756|
|Weblogic_CVE_2020_2883|
|Weblogic_0Day_1|
|Weblogic_CVE_2020_14883|
|FastJson_AutoType_ByPass|
|TongWeb|
|Landray_BeabShell_RCE|
|JBoss_CVE_2017_12149|
|JBoss_CVE_2017_7504|
|Confluence_CVE_2022_26134|
|Confluence_CVE_2021_26084|
|ECology_BeanShell_RCE|
|Seeyon_Unauthorized_RCE|
|SpringGateWay_CVE_2022_22947|


### Generate模块
![generate](https://github.com/suizhibo/MemShellGene/assets/28916595/d789f3fd-589e-48b3-aa4f-4f6580b977f0)



该模块涉及到ava的动态编译，需要正确配置java classpath，并确认该目录下的lib目录包含tools.jar。
使用该模块可以快速生成内存马的BASE64或者BCEL字符串。
![86bc73a1bbdb189912319f3e27afc060.png](en-resource://database/535:1)


#### 内存马版本测试
![d28b0c6bdad42b1453170a10aeb8cd75.png](en-resource://database/534:1)




#### 验证须知
1. Header：X-Requested-With: XmlHTTPRequest
2. 访问触发路径，若response返回的Success,说明内存马注入成功


#### 上线须知：

1. 密码: pAS3

2. 秘钥: key

3. Header：X-Requested-With: XMLHTTPRequest






