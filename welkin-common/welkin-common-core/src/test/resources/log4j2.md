## 1.日志脱敏配置

###1.1 添加依赖包

添加welkin-common-sensitive-xxx.jar依赖到项目中，项目中需要有 log4j-core，jackson-databind等相关依赖。

###1.2 添加log4j2自定义组件扫描配置
log4j2提供了自定义组件的功能，添加自定义组件需要将自定义组件类所在的包告知log4j2以通知配置扫描是能够加载自定义部分的组件。这里需要添加自定义的包路径到
log4j2 配置的根节点，即Configuration节点的packages属性中。以xml配置格式如下：

```XML

<Configuration status="OFF" packages="org.apache.logging.log4j.core">
</Configuration>
```

###1.3 普通日志脱敏
一般我们日志是通过PatternLayout来输出日志格式，我们通过自定义一个SensitivePatternLayout来增强log4j2原生的PatternLayout，在原来PatternLayout基础上添加RegexReplaces属性来定义一个正则表达式的集合用于适配多个正则拖米规则。样例如下：

```XML

<SensitivePatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss.SSS} [%traceId] [%t] %-5level %logger{1.} - %msg%n ">
    <RegexReplaces name="RegexReplaces">
        <replace regex="(&lt;IdNo>|&lt;CertId>|&lt;CertID>)(\d{3})\d{11,14}(\w{1}&lt;/)"
                 replacement="$1$2**************$3"/>
        <replace
                regex="(&lt;UserId>|&lt;FullName>|&lt;UserName>|&lt;AcName>|&lt;CifName>)([\u4E00-\u9FA5]{1})[\u4E00-\u9FA5]{1,}(&lt;/)"
                replacement="$1$2**$3"/>
        <replace
                regex="(&lt;MobilePhone>|&lt;BankBindPhone>|&lt;MobileTelephone>|&lt;FamilyTel>)(\d{3})\d{4}(\d{4}&lt;/)"
                replacement="$1$2****$3"/>
        <replace
                regex="(&lt;AcNo>|&lt;MyBankAccount>|&lt;LoanAccountNo>|&lt;BackAccountno>|&lt;EAcNo>)(\d{3})\d{10,13}(\d{3}&lt;/)"
                replacement="$1$2*************$3"/>
        <replace
                regex="(Phone|mobilePhone|phone|familyTel|holderMobile|mobileTelephone|bankBindPhone|holdermobile)(\\*&quot;:\\*&quot;)(\d{3})\d{4}(\d{4}\\*&quot;)"
                replacement="$1$2$3****$4"/>
        <replace
                regex="(cardId|id_card_no|idCardNo|holderIdNo|holder_id_no|idNo|certId|idCard|holderidno|certID)(\\*&quot;:\\*&quot;)(\d{3})\d{11,14}(\w{1}\\*&quot;)"
                replacement="$1$2$3**************$4"/>
        <replace
                regex="(name_pingyin|namePingyin|accountName|account_name|fullName|userId|realName)(\\*&quot;:\\*&quot;)([\u4E00-\u9FA5]{1})([\u4E00-\u9FA5]{1,})(\\*&quot;)"
                replacement="$1$2$3**$5"/>
        <replace regex="(card_no|cardNo|acNo)(\\*&quot;:\\*&quot;)(\d{3})\d{10,13}(\d{3}\\*&quot;)"
                 replacement="$1$2$3*************$4"/>
        <replace
                regex="(pwd|Pwd|PWD|pass|Pass|PASS|password|Password|PASSWORD)(\\*&quot;:\\*&quot;)(['`~!@#$%^&amp;*\(\)\w]{1,})(&quot;)"
                replacement="$1$2******$4"/>
        <replace regex="(email|Email|EMAIL)(\\*&quot;:\\*&quot;)(\w{3})(\w{1,})(@\w{1,}.\w{1,}&quot;)"
                 replacement="$1$2$3******$5"/>
    </RegexReplaces>
</SensitivePatternLayout> 
```

其中replace节点就是一个要匹配的正则脱敏规则。regex属性就是正则表达式来配置日志中需要脱敏的数据，replacement属性确定替换逻辑，前者通过正则表达式将匹配的数据切分成多段，后者替换需要脱敏的段数据即可实现脱敏逻辑。

以邮箱脱敏为例：

	脱敏前数据：{"email" :"123456789@163.com"}
	
	regex：(email|Email|EMAIL)(\\*&quot;:\\*&quot;)(\w{3})(\w{1,})(@\w{1,}.\w{1,}&quot;)
	
	匹配到的数据：email" :"123456789@163.com"
	
	正则切分结果：第一段"(email|Email|EMAIL)"匹配到 "email"; 第二段"(\\*&quot;:\\*&quot;)"匹配到 "\" :\""; 第三段"(\w{3})"匹配到"123"; 第四段"(\w{1,})"匹配到"456789"; 第四段"(@\w{1,}.\w{1,}&quot;)"匹配到"@163.com\""; 
	
	replacement：$1$2$3******$5,意思是保留第1,2,3,5变量值，使用"******"替换第4个变量值，那么根据上面的切分结果，"456789"的值将被替换成"******"
	
	最终输出结果：{"email" :"123******@163.com"}

一个完整的例子：

原始数据：

```
{ "id": 1, "username": "admin", "password": "1234`!@#$%^&*we56", "realname": "座山雕", "cardId": "6225800267798329", "email": "zuoshandiao@163.com", "idNo": "411323198708134633", "phone": "13520439383" }
```

脱敏结果

```
{ "id": 1, "username": "admin", "password": "******", "realname": "座**", "cardId": "622**************9", "email": "zuo******@163.com", "idNo": "411**************3", "phone": "135****9383" }
```

###1.4 JSON日志脱敏
类似于SensitivePatternLayout，我们自定义了SensitiveJsonLayout来增强了log4j2原生提供的JsonLayout以支持json格式的日志脱敏。为了配置统一SensitiveJsonLayout也使用正则的方式配置脱敏策略。

一个样例：

```XML

<SensitiveJsonLayout compact="true" eventEol="true"
                     properties="true" stacktraceAsString="true" objectMessageAsJsonObject="true">
    <KeyValuePair key="nanoTimestamp" value="$${UtcNanos:}"/>
    <RegexReplaces name="RegexReplaces">
        <replace regex="(&lt;IdNo>|&lt;CertId>|&lt;CertID>)(\d{3})\d{11,14}(\w{1}&lt;/)"
                 replacement="$1$2**************$3"/>
        <replace
                regex="(&lt;UserId>|&lt;FullName>|&lt;UserName>|&lt;AcName>|&lt;CifName>)([\u4E00-\u9FA5]{1})[\u4E00-\u9FA5]{1,}(&lt;/)"
                replacement="$1$2**$3"/>
        <replace
                regex="(&lt;MobilePhone>|&lt;BankBindPhone>|&lt;MobileTelephone>|&lt;FamilyTel>)(\d{3})\d{4}(\d{4}&lt;/)"
                replacement="$1$2****$3"/>
        <replace
                regex="(&lt;AcNo>|&lt;MyBankAccount>|&lt;LoanAccountNo>|&lt;BackAccountno>|&lt;EAcNo>)(\d{3})\d{10,13}(\d{3}&lt;/)"
                replacement="$1$2*************$3"/>
        <replace
                regex="(Phone|mobilePhone|phone|familyTel|holderMobile|mobileTelephone|bankBindPhone|holdermobile)(\\*&quot;:\\*&quot;)(\d{3})\d{4}(\d{4}\\*&quot;)"
                replacement="$1$2$3****$4"/>
        <replace
                regex="(cardId|id_card_no|idCardNo|holderIdNo|holder_id_no|idNo|certId|idCard|holderidno|certID)(\\*&quot;:\\*&quot;)(\d{3})\d{11,14}(\w{1}\\*&quot;)"
                replacement="$1$2$3**************$4"/>
        <replace
                regex="(name_pingyin|namePingyin|accountName|account_name|fullName|userId|realName)(\\*&quot;:\\*&quot;)([\u4E00-\u9FA5]{1})([\u4E00-\u9FA5]{1,})(\\*&quot;)"
                replacement="$1$2$3**$5"/>
        <replace regex="(card_no|cardNo|acNo)(\\*&quot;:\\*&quot;)(\d{3})\d{10,13}(\d{3}\\*&quot;)"
                 replacement="$1$2$3*************$4"/>
        <replace
                regex="(pwd|Pwd|PWD|pass|Pass|PASS|password|Password|PASSWORD)(\\*&quot;:\\*&quot;)(['`~!@#$%^&amp;*\(\)\w]{1,})(&quot;)"
                replacement="$1$2******$4"/>
        <replace regex="(email|Email|EMAIL)(\\*&quot;:\\*&quot;)(\w{3})(\w{1,})(@\w{1,}.\w{1,}&quot;)"
                 replacement="$1$2$3******$5"/>
    </RegexReplaces>
</SensitiveJsonLayout>
```

一个完整的例子：

原始数据：

```
{ "id": 1, "username": "admin", "password": "1234`!@#$%^&*we56", "realname": "座山雕", "cardId": "6225800267798329", "email": "zuoshandiao@163.com", "idNo": "411323198708134633", "phone": "13520439383" }
```

脱敏结果

```
{
  "instant": {
    "epochSecond": 1681122500,
    "nanoOfSecond": 527000000
  },
  "thread": "main",
  "level": "DEBUG",
  "loggerName": "com.ring.welkin.common.sensitive.SensitiveTests",
  "message": "{\"id\":1,\"username\":\"admin\",\"password\":\"******\",\"realname\":\"座**\",\"cardId\":\"622**************9\",\"email\":\"zuo******@163.com\",\"idNo\":\"411**************3\",\"phone\":\"135****9383\"}",
  "endOfBatch": false,
  "loggerFqcn": "org.apache.logging.slf4j.Log4jLogger",
  "contextMap": {
  },
  "threadId": 1,
  "threadPriority": 5,
  "nanoTimestamp": "${UtcNanos:}"
}
```