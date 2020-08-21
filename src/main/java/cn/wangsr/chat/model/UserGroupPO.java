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
@Entity(name= UserGroupPO.ENTITY_NAME)
public class UserGroupPO implements Serializable {
    public static final String ENTITY_NAME = "user_groups";
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "varchar(100) COMMENT '群聊名称'")
    private String groupName;
    @Column(columnDefinition = "varchar(255) COMMENT '头像'")
    private String avatarUrl;
    @Column(columnDefinition = "varchar(255) COMMENT '群聊用户name（按顿号、隔开）'")
    private String groupUsers;
    @Column(columnDefinition = "varchar(255) COMMENT '群聊用户Ids（按逗号,隔开）'")
    private String groupUsersIds;
    @CreationTimestamp
    private LocalDateTime createTime;
    @UpdateTimestamp
    private LocalDateTime updateTime;




}
