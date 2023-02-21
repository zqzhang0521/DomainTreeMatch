# 什么是 DomainTreeMatch
## 说明

DomainTreeMatch是使用二叉树来匹配字符串的算法。

## 适用场景/行业

电信，电商等行业，使用大数据技术，高效匹配用户访问的域名网站等。过滤后的结果集用以当前的业务，比如集成机器学习算法，推荐系统算法等。

## 举例

当我们有一些域名并为其分组，比如：

| 组名 | code | 域名          |
| ---- | ---- | ------------- |
| 组1  | 1    | www.baidu.com |
| 组1  | 2    | wap.baidu.com |
| 组2  | 3    | *.qq.com      |
| ...  | ...  | ...           |
| 组6  | 6    | www.sina.com  |

组名和code都是自定义的，但是code默认不能为-1（-1是特殊标识，可以修改）

现在我们有一些域名：比如测试用例中的（如下），需要判断这些域名是否在域名分组里，可以使用DomainTreeMatch来判断，-1标识不在。在大数据量情况下可以高效匹配。

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



# 为什么DomainTreeMatch效率高?



数据存储采用树状结构（NewTree），树包含节点信息（NewTreeNode），每个节点都含有节点信息。

优势：

- 二叉树支持动态的插入和查找，保证操作在O(height)时间，这就是完成了哈希表不便完成的工作，动态性。但是二叉树有可能出现worst-case，如果输入序列已经排序，则时间复杂度为O(N)

- 平衡二叉树/红黑树就是为了将查找的时间复杂度保证在O(logN)范围内，主要优点集中在快速查找。

缺点：

- 构建树结构时，采用Java 对象引用方式，在大数据量的情况下构建树，是非常耗时的。解决办法：看业务需求，域名数据是否经常变化，如果不经常变化可以在业务中，每天构建一次树。既保证域名数据的更新，也防止经常构建耗时。

# 使用

## 运行

TestTree类中，运行junit ，testDomainTree

## 结果

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



