#语法说明
只有以`$`或`$$`开头的才为定义量或标识量，否则为不可修改的常量。
## 预定义
 - `$`：（仅用于`JSONKey`）不提供基于WebUI面板的配置功能，需配置请自行修改配置文件 - `JSOONValue`写支持页面。
 - `$ALLOW_GROUP`
```perl5
allow_group     => [$$String, $$Long]
```

 - `$BAN_GROUP`
```perl5
ban_group     => [$$String, $$Long]
```

 - `$BAN_USER`
 ```perl5
ban_user     => [$$String, $$Long]
```
 - `$LISTEN`
```perl5
listen   => [{
    host     => "$$IPAddress",
    port     => $$IPPort
}]
```
**选项：-WithTLS**:   
```perl5
listen     => [{
    host           => "$$IPAddress",
    port           => $$IPPort,
    # 以下是选项部分
    tls            => $$TrueFalse,
    tls_ca         => $$Path,
    # 以下两列分别为两种方式，任一一种均可
    tls_cert       => '$$Path',
    tls_cert       => {'$$URL' => '$$Path'},
    tls_ciphers    => '$TLS_CIPHER',
    tls_verify     => $TLS_VERIFY,
    tls_version    => 'TLSv1_2'
}]
```
 - `$KEYWORD`
 ```perl5
keyword     => [qw($$Array:String)]
```
 - `$TLS_VERIFY`：`0x00`或`0x03`。官方解释：
    > TLS verification mode, defaults to 0x03 if a certificate authority file has been provided, or 0x00.
 - `$$KNOWLEDGE_BASE_MODE`：`$$String`，fuzzy/regex/exact，分别表示模糊|正则|精确,
## 标识量

基本类型：
 - `$$Int`：整形数字，同Java中`int`。
   - `Second`：秒。
 - `$$Long`：长整形，同Java中`int`。
 - `$$String`：字符串，同Java中`String`。
   - **特别注意实现`$$String`时要给每个`String`两边加上单引号！**
   - `Email`：电子邮箱地址。
   - `IPAddress`：IP地址，三位一组。
 - `$$TrueFalse`：0/1，即`$$Int`型。
 - `$$URL`：统一资源管理符、同Java中`java.io.net.URL`。
 - `$$FilePath`：文件路径，实现相当于Java中`File`。
 
包装类型：
 - `$$Array`：数组，可与以上任一基本类型组合。如`A,B,C`
   - **特别注意实现`$$Array:String`时要给每个`String`两边加上双引号！**
 - `$$Seq`：以空格分隔的数组，可与以上任一基本类型组合。如`A B C`
 - `$$Parameters`：包含多个子`Parameter`的`Parameter`