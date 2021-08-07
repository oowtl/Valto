package com.ssafy.db.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser_Room is a Querydsl query type for User_Room
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUser_Room extends EntityPathBase<User_Room> {

    private static final long serialVersionUID = 710510445L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser_Room user_Room = new QUser_Room("user_Room");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final QRoom roomId;

    public final QUser userId;

    public final BooleanPath userSide = createBoolean("userSide");

    public QUser_Room(String variable) {
        this(User_Room.class, forVariable(variable), INITS);
    }

    public QUser_Room(Path<? extends User_Room> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser_Room(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser_Room(PathMetadata metadata, PathInits inits) {
        this(User_Room.class, metadata, inits);
    }

    public QUser_Room(Class<? extends User_Room> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.roomId = inits.isInitialized("roomId") ? new QRoom(forProperty("roomId"), inits.get("roomId")) : null;
        this.userId = inits.isInitialized("userId") ? new QUser(forProperty("userId"), inits.get("userId")) : null;
    }

}

