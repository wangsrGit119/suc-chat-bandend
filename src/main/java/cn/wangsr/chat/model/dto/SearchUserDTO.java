package cn.wangsr.chat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author wjl
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserDTO implements Serializable {

    private Long userId;
    private String username;
    private String nickname;
    private String email;
    private String avatarUrl;
    /**
     * 是否为好友
     */
    private Boolean friendsOrNot;
}
