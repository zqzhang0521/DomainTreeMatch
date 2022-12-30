package bean;



import bean.group.GroupInfo;

import java.io.Serializable;

/**
 * @ClassName NewTreeNode
 * @Author zhangzq13
 **/

public class NewTreeNode implements Serializable {
    private static final long serialVersionUID = 1L;
    //存储数据,code存域信息，name存组名称，只有叶子节点才有组名称
    public GroupInfo data;
    //左节点  同一维度
    NewTreeNode leftNode;
    //右节点  同一维度
    NewTreeNode rightNode;
    //前一层节点  同一维度
    NewTreeNode parentNode;

    //下一维度节点，用于存储下层信息，不同维度
    NewTreeNode nextDimensionNode;
    //上一维度节点，用于存储上层信息，不同维度。只有一个维度的话，preDimensionNode就是root
//    NewTreeNode preDimensionNode;
    //泛域名的叶子节点标志
    public boolean ifLeafNode = false;

    //平衡
    char balence = '=';


    public NewTreeNode(GroupInfo data) {
        this.data = data;
        this.leftNode = null;
        this.rightNode = null;
        this.nextDimensionNode = null;
        this.parentNode = null;
    }


}
