package cn.wangsr.chat.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserGroupPO is a Querydsl query type for UserGroupPO
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserGroupPO extends EntityPathBase<UserGroupPO> {

    private static final long serialVersionUID = -1273221247L;

    public static final QUserGroupPO userGroupPO = new QUserGroupPO("userGroupPO");

    public final StringPath avatarUrl = createString("avatarUrl");

    public final DateTimePath<java.time.LocalDateTime> createTime = createDateTime("createTime", java.time.LocalDateTime.class);

    public final StringPath groupName = createString("groupName");

    public final StringPath groupUsers = createString("groupUsers");

    public final StringPath groupUsersIds = createString("groupUsersIds");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> updateTime = createDateTime("updateTime", java.time.LocalDateTime.class);

    public QUserGroupPO(String variable) {
        super(UserGroupPO.class, forVariable(variable));
    }

    public QUserGroupPO(Path<? extends UserGroupPO> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserGroupPO(PathMetadata metadata) {
        super(UserGroupPO.class, metadata);
    }

}

