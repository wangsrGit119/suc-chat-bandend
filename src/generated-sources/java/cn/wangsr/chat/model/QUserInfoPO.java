package cn.wangsr.chat.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserInfoPO is a Querydsl query type for UserInfoPO
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserInfoPO extends EntityPathBase<UserInfoPO> {

    private static final long serialVersionUID = 1813333002L;

    public static final QUserInfoPO userInfoPO = new QUserInfoPO("userInfoPO");

    public final StringPath avatarUrl = createString("avatarUrl");

    public final DateTimePath<java.time.LocalDateTime> createTime = createDateTime("createTime", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final StringPath familyAddress = createString("familyAddress");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath locationAddress = createString("locationAddress");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final DateTimePath<java.time.LocalDateTime> updateTime = createDateTime("updateTime", java.time.LocalDateTime.class);

    public final StringPath username = createString("username");

    public final NumberPath<Integer> uSex = createNumber("uSex", Integer.class);

    public QUserInfoPO(String variable) {
        super(UserInfoPO.class, forVariable(variable));
    }

    public QUserInfoPO(Path<? extends UserInfoPO> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserInfoPO(PathMetadata metadata) {
        super(UserInfoPO.class, metadata);
    }

}

