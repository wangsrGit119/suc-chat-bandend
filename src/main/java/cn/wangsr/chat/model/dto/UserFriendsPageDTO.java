package cn.wangsr.chat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户通讯录
 * @author wjl
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFriendsPageDTO implements Serializable {

    private Long userId;

    /**
     * 通讯录好用类型 1.好友  2.群组
     */
    private Integer type;
    /**
     * 聊天对象名称（好友名或者群组名称）
     */
    private String chatName;

    /**
     * 聊天对象头像
     */
    private String avatarUrl;




}
