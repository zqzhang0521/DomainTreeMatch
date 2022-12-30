package bean.group;

import lombok.Data;

import java.io.Serializable;

/**
 * 组信息，存储于树上
 *
 * @ClassName GroupInfo
 * @Author zhangzq13
 **/
@Data
public class GroupInfo implements Serializable {
    private String name;
    private String id;
    private String code;

    /***
     * @desc: 域名所用的构造，name：是组名，id是组id，code是存储的域信息
     * @MethodName: GroupInfo
     * @param: name
     * @param: code
     * @param: id
     * @Return:
     * @Author: zhangzq13
     **/
    public GroupInfo(String name, String id, String code) {
        this.name = name;
        this.id = id;
        this.code = code;
    }
    /***
     * @desc: ip所用的构造，name：是组名，id是组id
     * @MethodName: GroupInfo
     * @param: name
     * @param: id
     * @Return: 
     * @Author: zhangzq13
     **/
    public GroupInfo(String name, String id) {
        this.name = name;
        this.id = id;
    }
}
