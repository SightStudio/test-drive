package com.sight.start.kafka.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProduceController {

    private final ProduceService produceService;

    public ProduceController(ProduceService produceService) {
        this.produceService = produceService;
    }

    @GetMapping("/produce/{msg}")
    public String produceMessage(@PathVariable String msg) {
        return produceService.sendMessage(msg);
    }
}
