package com.sight.archunitstart;

import com.sight.archunitstart.config.ArchitectureTest;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

class DtoArchUnitTest extends ArchitectureTest {

    @Test
    @DisplayName("""
            Controller에서 상속된 DTO를 리턴하는것을 금지한다.
            """)
    void 컨트롤러_상속된_DTO_리턴_금지() {
        ArchRule rule = ArchRuleDefinition.methods()
                .that()
                .areDeclaredInClassesThat()
                .areAnnotatedWith(RestController.class).or()
                .areAnnotatedWith(Controller.class)
                .should(상속된_DTO_리턴_금지());
        rule.check(classes);
    }

    private ArchCondition<JavaMethod> 상속된_DTO_리턴_금지() {
        return new ArchCondition<>("Controller에서 상속된 DTO를 리턴하면 안됩니다.") {
            @Override
            public void check(JavaMethod method, ConditionEvents conditionEvents) {
                if (method.isMetaAnnotatedWith(RequestMapping.class)) {
                    JavaClass returnType = method.getRawReturnType();
                    returnType.getSuperclass()
                            .ifPresent(superclasses -> {
                                        String message = """
                                                Method %s in class %s returns a DTO that is inherited, which is not allowed. 다음 클래스를 상속받고 있음 [%s]
                                                """.formatted(
                                                method.getName(),
                                                method.getOwner().getFullName(),
                                                String.join(", ", superclasses.getName()
                                                )
                                        );
                                        conditionEvents.add(SimpleConditionEvent.violated(method, message));
                                    }
                            );
                }
            }
        };
    }
}
