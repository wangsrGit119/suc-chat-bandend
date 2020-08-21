package cn.wangsr.chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author wjl
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
@Entity(name= UserMessagePO.ENTITY_NAME)
public class UserMessagePO implements Serializable {
    public static final String ENTITY_NAME = "user_message";
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "varchar(100) COMMENT '消息'")
    private String message;
    @Column(columnDefinition = "varchar(255) COMMENT '图片消息url'")
    private String imageUrl;

    @Column(columnDefinition = "int(20) COMMENT '（用户或者群聊ID）'")
    private Long userId;
    @Column(columnDefinition = "int(20) COMMENT '聊天对象（用户或者群聊ID）'")
    private Long bindTarget;
    @Column(columnDefinition = "int(4) COMMENT '消息类型 1.点对点  2.群发'")
    private Integer messageType;
    @CreationTimestamp
    private LocalDateTime createTime;
    @UpdateTimestamp
    private LocalDateTime updateTime;




}
