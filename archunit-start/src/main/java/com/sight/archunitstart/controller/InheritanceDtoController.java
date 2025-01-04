package com.sight.archunitstart.controller;

import com.sight.archunitstart.dto.ActorExtension;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class InheritanceDtoController {

    /**
     * 상속된 형태를 가진 DTO를 리턴하는 케이스
     * ArchUnit에서 해당 케이스를 파악하고, 테스트에서 Fail 시켜야한다.
     */
    @GetMapping("/get-some-data")
    public ActorExtension returnInheritDto() {
        Optional

        return new ActorExtension();
    }
}
