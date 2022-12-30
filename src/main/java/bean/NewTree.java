package bean;

import bean.group.GroupInfo;
import enums.CommonCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 用法：请调用构造函数构建对象
 *
 * @ClassName DomainTree
 * @Author zhangzq13
 **/
@Data
public class NewTree implements Serializable {
    private static final long serialVersionUID = 1L;
    private NewTreeNode root;
    //不在树上，默认存储的组信息
    private GroupInfo defaultGroupInfo;

    /***
     * @desc: 匹配不到域名组时候，默认使用defaultGroupInfo的name和code
     * @MethodName: NewTree
     * @param: defaultGroupInfo
     * @Return:
     * @Author: zhangzq13
     **/
    public NewTree(GroupInfo defaultGroupInfo) {
        if (null == defaultGroupInfo) {
            this.defaultGroupInfo = new GroupInfo(CommonCode.Other.getName(), CommonCode.Other.getCode(),CommonCode.Other.getCode());
        } else {
            this.defaultGroupInfo = defaultGroupInfo;
        }
    }

    /***
     * @desc: 前序遍历
     * @MethodName: PreOrder
     * @param: domainNames
     * @Return: com.ai.beans.group.GroupInfo
     * @Author: zhangzq13
     **/
    public GroupInfo PreOrder(String domainNames){
        String[] domainSplit = null;
        if (".".equals(domainNames)) {
            domainSplit = new String[]{"."};
        }else {
            domainSplit = domainNames.split("\\.");
        }
        String code = defaultGroupInfo.getCode();
        String name = defaultGroupInfo.getName();
        String id = defaultGroupInfo.getId();
        NewTreeNode newTreeNode = root;
        for (int i = domainSplit.length - 1; i >= 0; i--) {
            newTreeNode = preOrder(newTreeNode, domainSplit[i]);
            //匹配不到，或者匹配到其他，则跳出
            if (null == newTreeNode || defaultGroupInfo.getCode().equals(newTreeNode.data.getCode())) {
                break;
            }
            //精确域名需要匹配到最后,或者匹配中泛域名
            if (i == 0 || "*".equals(newTreeNode.data.getCode())) {
                code = newTreeNode.data.getCode();
                name = newTreeNode.data.getName();
                id = newTreeNode.data.getId();
            }

            newTreeNode = newTreeNode.nextDimensionNode;
        }
        return new GroupInfo(name, id,code);
    }

    /***
     * @desc: 前序遍历
     * @MethodName: preOrder
     * @param: next
     * @param: domain
     * @Return: com.ai.beans.tree.NewTreeNode
     * @Author: zhangzq13
     **/
    public NewTreeNode preOrder(NewTreeNode next, String domain){
        for (; next != null; ) {
            String nextDomainCode = next.data.getCode();
            //如果下一层级有*，直接命中并返回
            if ("*".equals(nextDomainCode)) {
                return next;
            }
            //比较结果
            int cpmResult = domain.compareTo(nextDomainCode);
            if (cpmResult > 0) {
                next = next.leftNode;
            } else if (cpmResult < 0) {
                next = next.rightNode;
            } else {
                //匹配到本层级
                return next;
            }
        }
        //匹配不到
        return null;
    }

    /***
     * @desc: 域名树上添加节点
     * @MethodName: addNode
     * @param: currNode
     * @param: groupName
     * @param: domain
     * @param: id
     * @Return: com.ai.beans.tree.NewTreeNode
     * @Author: zhangzq13
     **/
    public NewTreeNode addNode(NewTreeNode currNode, String groupName, String id,String domain) {
        //域名树上挂载组信息
        GroupInfo groupInfo = new GroupInfo(groupName,id,domain);
        //上一维度的连接节点
        NewTreeNode preDimensionNode = null;
        boolean rootFlag = false;
        if (null == root) {
            root = new NewTreeNode(groupInfo);
            return root;
        } else {
            NewTreeNode temp = null;
            if (null == currNode) {
                preDimensionNode = root;
                rootFlag = true;
                //优先遍历树上是否有数据,顶级域
                NewTreeNode resultNode = preOrder(root, domain);
                temp = null == resultNode ? root : resultNode;
            } else {
                //当前传入泛域名
                if ("*".equals(domain)) {
                    currNode.nextDimensionNode = new NewTreeNode(groupInfo);
                    currNode.nextDimensionNode.rightNode = null;
                    currNode.nextDimensionNode.leftNode = null;
                    currNode.nextDimensionNode.ifLeafNode = true;
                    currNode.nextDimensionNode.balence = '=';
                    return currNode.nextDimensionNode;
                }
                //是否有下一维度
                if (null == currNode.nextDimensionNode) {
                    currNode.nextDimensionNode = new NewTreeNode(groupInfo);
                    temp = currNode.nextDimensionNode;
                } else {
                    //优先遍历下一维度树上是否有数据
                    NewTreeNode resultNode = preOrder(currNode.nextDimensionNode, domain);
                    //树上没有的情况下，构建下一维度
                    if (null == resultNode) {
                        temp = currNode.nextDimensionNode;
                    } else {
                        temp = resultNode;
                    }
                }
                //调用时需要  preDimensionNode.nextDimensionNode
                preDimensionNode = currNode;
            }

            String tmpDomain = temp.data.getCode();
            //当前层级为泛域名的情况,左右及其下一层级均为空
            if ("*".equals(tmpDomain)) {
                temp.nextDimensionNode = null;
                temp.rightNode = null;
                temp.leftNode = null;
                temp.ifLeafNode = true;
                temp.balence = '=';
                return temp;
            }

            NewTreeNode ancestor = null;
            int comp = 0;
            while (true) {
                //重新获取tmpDomain
                tmpDomain = temp.data.getCode();
                comp = domain.compareTo(tmpDomain);
                if (comp == 0) {
                    return temp;
                    //插入到左节点
                } else if (comp > 0) {
                    if (temp.balence != '=') {
                        ancestor = temp;
                    }

                    if (temp.leftNode == null) {

                        if ((temp.parentNode != null && temp.parentNode.balence == 'L' && temp.parentNode.rightNode != null
                                && temp.parentNode.rightNode.equals(temp))
                                || (temp.parentNode != null && temp.parentNode.balence == 'R' && temp.parentNode.leftNode != null
                                && temp.parentNode.leftNode.equals(temp))) {
                            ancestor = null;
                            temp.parentNode.balence = '=';
                        }
                        NewTreeNode newnode = new NewTreeNode(groupInfo);
                        temp.leftNode = newnode;
                        newnode.parentNode = temp;
                        fix(rootFlag,preDimensionNode,ancestor, temp.leftNode);
                        return newnode;
                    } else {
                        temp = temp.leftNode;
                    }
                    //插入到右节点
                } else {
                    if (temp.balence != '=') {
                        ancestor = temp;
                    }
                    if (temp.rightNode == null) {

                        if ((temp.parentNode != null && temp.parentNode.balence == 'L' && temp.parentNode.rightNode != null
                                && temp.parentNode.rightNode.equals(temp))
                                || (temp.parentNode != null && temp.parentNode.balence == 'R' && temp.parentNode.leftNode != null
                                && temp.parentNode.leftNode.equals(temp))) {
                            ancestor = null;
                            temp.parentNode.balence = '=';
                        }
                        count(temp.parentNode);
                        NewTreeNode newnode = new NewTreeNode(groupInfo);
                        temp.rightNode = newnode;
                        newnode.parentNode = temp;
                        fix(rootFlag,preDimensionNode,ancestor, temp.rightNode);
                        return newnode;
                    } else {
                        temp = temp.rightNode;
                    }
                }
            }
        }
    }



    /***
     * @desc: 用于插入新的结点后重新将整棵树旋转成为新的平衡树
     * @MethodName: fix
     * @param: rootFlag 是否root节点
     * @param: preDimensionNode
     * @param: node
     * @param: addedNode
     * @Return: void
     * @Author:
     **/
    public void fix(boolean rootFlag, NewTreeNode preDimensionNode,NewTreeNode node, NewTreeNode addedNode) {
        NewTreeNode tmpRoot = root;
        if (!rootFlag) {
            tmpRoot = preDimensionNode.nextDimensionNode;
        }
        count(tmpRoot);
        if (node == null) {
            count(tmpRoot);
            return;
        }



        if (node.balence == 'L') {
            if (node.leftNode.data.getCode().compareTo(addedNode.data.getCode()) > 0) // 左子树的左子树
            {
                rightTurn(rootFlag,preDimensionNode,node);
            } else // 左子树中的右子树，要旋转两次
            {
                leftTurn(rootFlag,preDimensionNode,node.leftNode);
                rightTurn(rootFlag,preDimensionNode,node);
            }
        } else if (node.balence == 'R') {
            if (node.rightNode.data.getCode().compareTo(addedNode.data.getCode()) > 0) {
                rightTurn(rootFlag,preDimensionNode,node.rightNode);
                leftTurn(rootFlag,preDimensionNode,node);
            } else {
                leftTurn(rootFlag,preDimensionNode,node);
            }
        }
        count(tmpRoot);
    }


    /***
     * @desc: 重新计算以root为头的树的平衡度
     * @MethodName: count
     * @param: tmpRoot
     * @Return: int
     **/
    public int count(NewTreeNode tmpRoot){
        if (tmpRoot == null) {
            return 0;
        }
        int left = count(tmpRoot.leftNode);
        int right = count(tmpRoot.rightNode);
        int re = left - right;
        if (re == 0) {
            tmpRoot.balence = '=';
            re = left;
        } else if (re > 0) {
            tmpRoot.balence = 'L';
            re = left;
        } else {
            tmpRoot.balence = 'R';
            re = right;
        }
        return 1 + re;
    }
    /***
     * @desc: 将以n为根的子树左转
     * @MethodName: leftTurn
     * @param: rootFlag 是否root节点
     * @param: preDimensionNode 上层节点
     * @param: n
     * @Return: void
     **/
    public void leftTurn(boolean rootFlag, NewTreeNode preDimensionNode,NewTreeNode n){
        NewTreeNode temp = n.rightNode;
        if (temp != null) {
            n.rightNode = temp.leftNode;
            if (temp.leftNode != null) {
                temp.leftNode.parentNode = n;
            }
            temp.parentNode = n.parentNode;
            if (n.parentNode == null) {
                if (rootFlag) {
                    root = temp;
                }else {
                    preDimensionNode.nextDimensionNode = temp;
                }
            } else {
                if (n.parentNode.rightNode == n) {
                    temp.parentNode.rightNode = temp;
                } else {
                    temp.parentNode.leftNode = temp;
                }
            }
            temp.leftNode = n;
            n.parentNode = temp;
        }

    }

    /***
     * @desc: 将以n为根的子树右转
     * @MethodName: rightTurn
     * @param: rootFlag 是否root节点
     * @param: preDimensionNode 上层节点
     * @param: n
     * @Return: void
     **/
    public void rightTurn(boolean rootFlag, NewTreeNode preDimensionNode,NewTreeNode n) {
        NewTreeNode temp = n.leftNode;
        if (temp != null) {
            n.leftNode = temp.rightNode;
            if (temp.rightNode != null) {
                temp.rightNode.parentNode = n;
            }
            temp.parentNode = n.parentNode;
            if (n.parentNode == null) {
                if (rootFlag) {
                    root = temp;
                }else {
                    preDimensionNode.nextDimensionNode = temp;
                }

            } else {
                if (n.parentNode.leftNode == n) {
                    temp.parentNode.leftNode = temp;
                } else {
                    temp.parentNode.rightNode = temp;
                }
            }
            temp.rightNode = n;
            n.parentNode = temp;
        }

    }
}
