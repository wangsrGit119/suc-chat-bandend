package cn.wangsr.chat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author wjl
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO implements Serializable {
    private String username;
    private String avatarUrl;
    private String nickname;
    private String imageUrl;
    private LocalDateTime createTime;
    private String message;

}
