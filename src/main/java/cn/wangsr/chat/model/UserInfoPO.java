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
@Entity(name= UserInfoPO.ENTITY_NAME)
public class UserInfoPO implements Serializable {
    public static final String ENTITY_NAME = "users";
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "varchar(100) COMMENT '用户名'")
    private String username;
    @Column(columnDefinition = "varchar(255) COMMENT '密码'")
    private String password;
    @Column(columnDefinition = "int(4) COMMENT '性别'")
    private Integer uSex;
    @Column(columnDefinition = "varchar(255) COMMENT '昵称'")
    private String nickname;
    @Column(columnDefinition = "varchar(255) COMMENT '头像地址'")
    private String avatarUrl;
    @Column(columnDefinition = "varchar(255) COMMENT '邮件'")
    private String email;
    @Column(columnDefinition = "varchar(255) COMMENT '家庭住址'")
    private String familyAddress;
    @Column(columnDefinition = "varchar(255) COMMENT '现居地址'")
    private String locationAddress;

    @CreationTimestamp
    private LocalDateTime createTime;
    @UpdateTimestamp
    private LocalDateTime updateTime;

//    @Column(columnDefinition = "int(10) DEFAULT 0 COMMENT '版本号'")
//    @Version
//    private Integer version = 0;



}
