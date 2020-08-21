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
@Entity(name= UserFriendsPO.ENTITY_NAME)
public class UserFriendsPO implements Serializable {
    public static final String ENTITY_NAME = "user_friends";
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "varchar(100) COMMENT '好友备注'")
    private String noteName;
    @Column(columnDefinition = "int(20) COMMENT '用户Id'")
    private Long userId;
    @Column(columnDefinition = "int(20) COMMENT '好友Id'")
    private Long partnerId;
    @Column(columnDefinition = "int(10) COMMENT '1.待同意 2.已同意'")
    private Integer type;
    @Column(columnDefinition = "int(10) COMMENT '1.申请人 2.被申请人'")
    private Integer belong;
    @CreationTimestamp
    private LocalDateTime createTime;
    @UpdateTimestamp
    private LocalDateTime updateTime;




}
