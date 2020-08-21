package cn.wangsr.chat.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserMessagePO is a Querydsl query type for UserMessagePO
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserMessagePO extends EntityPathBase<UserMessagePO> {

    private static final long serialVersionUID = 543183433L;

    public static final QUserMessagePO userMessagePO = new QUserMessagePO("userMessagePO");

    public final NumberPath<Long> bindTarget = createNumber("bindTarget", Long.class);

    public final DateTimePath<java.time.LocalDateTime> createTime = createDateTime("createTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final StringPath message = createString("message");

    public final NumberPath<Integer> messageType = createNumber("messageType", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> updateTime = createDateTime("updateTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUserMessagePO(String variable) {
        super(UserMessagePO.class, forVariable(variable));
    }

    public QUserMessagePO(Path<? extends UserMessagePO> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserMessagePO(PathMetadata metadata) {
        super(UserMessagePO.class, metadata);
    }

}

