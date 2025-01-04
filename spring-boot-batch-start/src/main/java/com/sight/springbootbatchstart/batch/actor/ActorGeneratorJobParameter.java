package com.sight.springbootbatchstart.batch.actor;

import java.time.LocalDateTime;

public interface ActorGeneratorJobParameter {
    LocalDateTime invokeDateTime();

    Long actorCount();
}

/**
 * 자바 record 는 CGLIB 프록시 못쓰니까 JDK 다이나믹 프록시로 바꾼다.
 * @param invokeDateTime
 * @param actorCount
 */
record ActorGeneratorJobParameterImpl(
        LocalDateTime invokeDateTime,
        Long actorCount
) implements ActorGeneratorJobParameter {

}
