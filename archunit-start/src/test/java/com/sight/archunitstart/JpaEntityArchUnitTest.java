package com.sight.archunitstart;

import com.sight.archunitstart.config.ArchitectureTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import jakarta.persistence.Entity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.sight.archunitstart.config.ArchUnitUtils.instant_타입_사용_금지_조건;
import static com.sight.archunitstart.config.ArchUnitUtils.transient_애너테이션_사용_금지_조건;

class JpaEntityArchUnitTest extends ArchitectureTest {

    @Test
    @DisplayName("""
            JPA Entity는 클래스명이 반드시 Entity로 끝나야한다.
            """)
    void 엔티티_네이밍_체크() {
        ArchRule rule = ArchRuleDefinition.classes()
                .that().areAnnotatedWith(Entity.class)
                .should().haveSimpleNameEndingWith("Entity");
        rule.check(classes);
    }

    @Test
    @DisplayName("""
            JPA Entity에서 필드에 Instant 타입은 허용되지 않는다.
            - Instant 대신 LocalDateTime, LocalDate 사용하자
            """)
    void 엔티티_필드_타입_체크_Instant() {
        ArchRule rule = ArchRuleDefinition.classes()
                .that().areAnnotatedWith(Entity.class)
                .should(instant_타입_사용_금지_조건());

        rule.check(classes);
    }

    @Test
    @DisplayName("""
            JPA Entity에서 필드에 Transient 애너테이션은 허용되지 않는다.
            - 쓸거면 별도의 DTO로 분리해서 사용하도록하자. 엔티티에는 DB 컬럼에 매핑되어야하는 필드만 있어야한다. 
            """)
    void 엔티티_Transient_금지_체크() {
        ArchRule rule = ArchRuleDefinition.classes()
                .that().areAnnotatedWith(Entity.class)
                .should(transient_애너테이션_사용_금지_조건());

        rule.check(classes);
    }
}
