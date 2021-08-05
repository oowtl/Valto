package com.ssafy.db.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRoom_History is a Querydsl query type for Room_History
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRoom_History extends EntityPathBase<Room_History> {

    private static final long serialVersionUID = 1752760786L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRoom_History room_History = new QRoom_History("room_History");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Short> action = createNumber("action", Short.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final DateTimePath<java.util.Date> insertedTime = createDateTime("insertedTime", java.util.Date.class);

    public final QRoom roomId;

    public final QUser userId;

    public QRoom_History(String variable) {
        this(Room_History.class, forVariable(variable), INITS);
    }

    public QRoom_History(Path<? extends Room_History> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRoom_History(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRoom_History(PathMetadata metadata, PathInits inits) {
        this(Room_History.class, metadata, inits);
    }

    public QRoom_History(Class<? extends Room_History> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.roomId = inits.isInitialized("roomId") ? new QRoom(forProperty("roomId"), inits.get("roomId")) : null;
        this.userId = inits.isInitialized("userId") ? new QUser(forProperty("userId"), inits.get("userId")) : null;
    }

}

