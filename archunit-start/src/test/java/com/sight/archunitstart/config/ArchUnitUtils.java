package com.sight.archunitstart.config;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import jakarta.persistence.Transient;

import java.time.Instant;

public class ArchUnitUtils {

    public static ArchCondition<JavaClass> instant_타입_사용_금지_조건() {
        return new ArchCondition<>("Instant 는 필드타입에서 사용 할 수 없다.") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents conditionEvents) {
                for (JavaField field : javaClass.getFields()) {
                    if (field.getRawType().isAssignableTo(Instant.class)) {
                        String message = String.format("Field %s in class %s uses Instant, which is not allowed.",
                                field.getName(), field.getOwner().getFullName()
                        );
                        conditionEvents.add(SimpleConditionEvent.violated(field, message));
                    }
                }
            }
        };
    }

    public static ArchCondition<JavaClass> transient_애너테이션_사용_금지_조건() {
        return new ArchCondition<>("@Transient 애너테이션은 프로젝트에서 사용 할 수 없다.") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents conditionEvents) {
                for (JavaField field : javaClass.getFields()) {
                    if (field.isAnnotatedWith(Transient.class)) {
                        String message = String.format("Field %s in class %s uses Transient, which is not allowed.",
                                field.getName(), field.getOwner().getFullName()
                        );
                        conditionEvents.add(SimpleConditionEvent.violated(field, message));
                    }
                }
            }
        };
    }



}
