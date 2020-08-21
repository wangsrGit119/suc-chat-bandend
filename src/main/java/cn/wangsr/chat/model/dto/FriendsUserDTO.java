package cn.wangsr.chat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 待处理好友信息（好友申请）
 * @author wjl
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendsUserDTO implements Serializable {

    private Long userId;
    private String username;
    private String nickname;
    private String email;
    private String avatarUrl;

}
