# DomainTreeMatch
use binary tree match domain

当我们有一些域名并为其分组，比如：

| 组名 | code | 域名          |
| ---- | ---- | ------------- |
| 组1  | 1    | www.baidu.com |
| 组1  | 2    | wap.baidu.com |
| 组2  | 3    | *.qq.com      |
| ...  | ...  | ...           |
| 组6  | 6    | www.sina.com  |

组名和code都是自定义的，但是code默认不能为-1（-1是特殊标识，可以修改）

现在我们有一些域名：比如测试用例中的：

```java
"www.hello2.com",
"a",
".",
",",
"www.qq2.com",
"www1.qq3.com",
"www2.sina.edu",
"*.baidu.top",
"www.baidu.top",
"com",
"a.com",
"1.www.baidu.com",
"ad4.sina.com.cn",
"app2.finance.sina.com.cn",
"1.www.baidu.com"
```

需要判断这些域名是否在域名分组里，可以使用DomainTreeMatch来判断，-1标识不在。在大数据量情况下可以高效匹配。



# how to use 

TestTree类中，运行junit ，testDomainTree

# result

```java
当前域名：【www.hello2.com】，是否在树上：false
当前域名：【a】，是否在树上：true
当前域名：【.】，是否在树上：true
当前域名：【,】，是否在树上：true
当前域名：【www.qq2.com】，是否在树上：true
当前域名：【www1.qq3.com】，是否在树上：false
当前域名：【www2.sina.edu】，是否在树上：false
当前域名：【*.baidu.top】，是否在树上：true
当前域名：【www.baidu.top】，是否在树上：true
当前域名：【com】，是否在树上：true
当前域名：【a.com】，是否在树上：true
当前域名：【1.www.baidu.com】，是否在树上：false
当前域名：【ad4.sina.com.cn】，是否在树上：false
当前域名：【app2.finance.sina.com.cn】，是否在树上：false
当前域名：【1.www.baidu.com】，是否在树上：false

Process finished with exit code 0

```



