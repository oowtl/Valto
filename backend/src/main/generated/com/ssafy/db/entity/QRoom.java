package com.ssafy.db.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRoom is a Querydsl query type for Room
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRoom extends EntityPathBase<Room> {

    private static final long serialVersionUID = 846449565L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRoom room = new QRoom("room");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final DateTimePath<java.util.Date> localDateTime = createDateTime("localDateTime", java.util.Date.class);

    public final NumberPath<Integer> observers = createNumber("observers", Integer.class);

    public final NumberPath<Integer> participants = createNumber("participants", Integer.class);

    public final StringPath roomPassword = createString("roomPassword");

    public final NumberPath<Integer> times = createNumber("times", Integer.class);

    public final StringPath title = createString("title");

    public final StringPath topicAgree = createString("topicAgree");

    public final StringPath topicOpposite = createString("topicOpposite");

    public final QUser userId;

    public QRoom(String variable) {
        this(Room.class, forVariable(variable), INITS);
    }

    public QRoom(Path<? extends Room> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRoom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRoom(PathMetadata metadata, PathInits inits) {
        this(Room.class, metadata, inits);
    }

    public QRoom(Class<? extends Room> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.userId = inits.isInitialized("userId") ? new QUser(forProperty("userId"), inits.get("userId")) : null;
    }

}

