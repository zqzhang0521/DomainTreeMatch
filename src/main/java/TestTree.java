import bean.NewTree;
import bean.NewTreeNode;
import bean.group.GroupInfo;
import enums.CommonCode;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName TestTree
 * @Author zhangzq13
 **/
public class TestTree {
    @Test
    public void testDomainTree(){
        List<GroupInfo> domains = Arrays.asList(
                new GroupInfo("组1","11","www.a.com"),
                new GroupInfo("组1","11","com"),
                new GroupInfo("组2","12","sina.com.cn"),
                new GroupInfo("组2","1","zzq3.zzq2.zzq1"),
                new GroupInfo("组2","1","a"),
                new GroupInfo("组3","3",","),
                new GroupInfo("组3","1","."),
                new GroupInfo("组3","1","www.qq.com"),
                new GroupInfo("组6","1","www.qq2.com"),
                new GroupInfo("组6","1","www.qq3.com"),
                new GroupInfo("组6","1","www.baidu.com"),
                new GroupInfo("组6","233","*.gccdn.net"),
//                //情况1  泛域名在最后
//                new GroupInfo("百度2","1","www1.baidu.top"),
//                new GroupInfo("百度","1","a.b.baidu.top"),
//                new GroupInfo("百度1","1","*.baidu.top")
//                //情况2  泛域名在中间
                new GroupInfo("百度2","1","www1.baidu.top"),
                new GroupInfo("百度1","1","*.baidu.top"),
                new GroupInfo("百度","1","a.b.baidu.top")
//                //情况2  泛域名在最前
//                new GroupInfo("百度1","*.baidu.top"),
//                new GroupInfo("百度2","www1.baidu.top"),
//                new GroupInfo("百度","a.b.baidu.top")
        ) ;
        GroupInfo defaultGroup = new GroupInfo("", CommonCode.Other.getCode(),CommonCode.Other.getCode());
        NewTree tree = getNewTree(domains,defaultGroup);

        List<String> result = Arrays.asList(
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
        );
        for (String tmpDomain : result) {
            String code = tree.PreOrder(tmpDomain).getCode();
            System.out.println("当前域名：【"+tmpDomain+"】，是否在树上："+!CommonCode.Other.getCode().equals(code));
        }
    }

    public static NewTree getNewTree(List<GroupInfo> domains, GroupInfo defaultGroup ){
        NewTree tree = new NewTree(defaultGroup);
        for (GroupInfo groupInfo : domains) {
            NewTreeNode newTreeNode = null;
            String domainName = groupInfo.getCode();
            String groupName = groupInfo.getName();
            String domainGroupId = groupInfo.getId();
            String[] domainSplit = null;
            if (".".equals(domainName)) {
                domainSplit = new String[]{"."};
            }else {
                domainSplit = domainName.split("\\.");
            }
            for (int i = domainSplit.length-1; i >= 0; i--) {
                //仅叶子节点挂载组名和组id
                //仅叶子节点挂载组名和组id，否则默认组名
                String tmpGroupName = defaultGroup.getName();
                String tmpGroupId = defaultGroup.getId();
                if (i == 0) {
                    tmpGroupName = groupName;
                    tmpGroupId = domainGroupId;
                }
                newTreeNode = tree.addNode(newTreeNode,tmpGroupName ,tmpGroupId, domainSplit[i]);
                if (i == 0) {
                    newTreeNode.data.setName(groupName);
                    newTreeNode.data.setId(domainGroupId);
                }

                if (newTreeNode.ifLeafNode) {
                    break;
                }

            }
        }
        return tree;
    }
}
