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

![1111111111](https://github.com/suizhibo/MemShellGene/assets/28916595/ddfcc2a9-dd20-4175-8485-2139e79bb600)



#### 内存马版本测试

![image](https://github.com/suizhibo/MemShellGene/assets/28916595/92452286-d0e0-41d7-8baf-9dd7ae62d7d8)





#### 验证须知
1. Header：X-Requested-With: XmlHTTPRequest
2. 访问触发路径，若response返回的Success,说明内存马注入成功


#### 上线哥斯拉须知：

1. 密码: pAS3

2. 秘钥: key

3. Header：X-Requested-With: XMLHTTPRequest

   ![图片1](https://github.com/suizhibo/MemShellGene/assets/28916595/e6d8a13b-b0f7-4562-84a0-af57e774beb5)



# 免责声明
1. 娱乐用途优先：本工具设计的初衷是为了教育、研究和娱乐目的。它可以帮助您了解网络安全的基础知识，提升您的技能，以及在合法范围内进行测试。

2. 请勿非法使用：请记住，未经授权对他人的系统、网络或设备进行渗透测试是违法的。我们强烈建议并恳求您不要使用本工具进行任何非法活动。否则，后果自负（并且可能会有法律追究哦）。

3. 合法授权：请确保您仅在获得明确授权的情况下使用本工具进行渗透测试。这包括但不限于：您自己的设备和网络，或明确授权您进行测试的第三方。

4. 知识与责任并重：网络安全是一项崇高的事业，保护网络安全是每一个网络安全爱好者的责任。使用本工具进行合法的测试和研究，帮助提升整体网络安全水平，这才是我们共同的目标。

5. 技术支持有限：本工具提供“按现状”提供，我们不对其适用性或可能造成的任何损失负责。使用本工具之前，请确保您已充分了解其功能和潜在影响。

6. 玩得开心，但要有节制：我们希望您在使用本工具时能获得乐趣并学到新知识，但请务必保持理智和克制。不要因为一时兴起而越界，记住，网络冒险有时也是需要勇气的撤退。

最后提醒
当您点击下载或使用本工具时，即表示您已阅读并同意以上所有条款。请戴好您的虚拟防护帽，系紧您的安全带，准备好迎接一场合法且富有教育意义的网络冒险吧！




