package cn.wangsr.chat.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserFriendsPO is a Querydsl query type for UserFriendsPO
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserFriendsPO extends EntityPathBase<UserFriendsPO> {

    private static final long serialVersionUID = 818180183L;

    public static final QUserFriendsPO userFriendsPO = new QUserFriendsPO("userFriendsPO");

    public final NumberPath<Integer> belong = createNumber("belong", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> createTime = createDateTime("createTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath noteName = createString("noteName");

    public final NumberPath<Long> partnerId = createNumber("partnerId", Long.class);

    public final NumberPath<Integer> type = createNumber("type", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> updateTime = createDateTime("updateTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUserFriendsPO(String variable) {
        super(UserFriendsPO.class, forVariable(variable));
    }

    public QUserFriendsPO(Path<? extends UserFriendsPO> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserFriendsPO(PathMetadata metadata) {
        super(UserFriendsPO.class, metadata);
    }

}

