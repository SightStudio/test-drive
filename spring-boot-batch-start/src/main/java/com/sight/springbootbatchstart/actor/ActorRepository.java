package com.sight.springbootbatchstart.actor;

import org.jooq.DSLContext;
import org.jooq.generated.tables.JActor;
import org.jooq.generated.tables.daos.ActorDao;
import org.jooq.generated.tables.pojos.Actor;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ActorRepository {

    private final DSLContext dslContext;
    private final ActorDao dao;
    private final JActor ACTOR = JActor.ACTOR;

    public ActorRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
        this.dao = new ActorDao(dslContext.configuration());
    }

    public void insert(List<Actor> actors) {
        var rows = actors.stream()
                .map(actor ->
                        DSL.row(
                                actor.getFirstName(),
                                actor.getLastName(),
                                actor.getLastUpdate()
                        )
                ).toList();

        dslContext.insertInto(ACTOR, ACTOR.FIRST_NAME, ACTOR.LAST_NAME, ACTOR.LAST_UPDATE)
                .valuesOfRows(rows)
                .execute();
    }
}
