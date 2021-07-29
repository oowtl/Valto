package com.ssafy.db.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserRecord is a Querydsl query type for UserRecord
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserRecord extends EntityPathBase<UserRecord> {

    private static final long serialVersionUID = 195998014L;

    public static final QUserRecord userRecord = new QUserRecord("userRecord");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Integer> draw = createNumber("draw", Integer.class);

    //inherited
    public final NumberPath<Integer> id = _super.id;

    public final NumberPath<Integer> lose = createNumber("lose", Integer.class);

    public final StringPath userId = createString("userId");

    public final NumberPath<Integer> win = createNumber("win", Integer.class);

    public QUserRecord(String variable) {
        super(UserRecord.class, forVariable(variable));
    }

    public QUserRecord(Path<? extends UserRecord> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserRecord(PathMetadata metadata) {
        super(UserRecord.class, metadata);
    }

}

