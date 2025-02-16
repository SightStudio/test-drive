package com.gc_demo.modern_jvm.web;

import com.gc_demo.modern_jvm.AllocateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RunnerController {

    private final AllocateService allocateService;

    public RunnerController(AllocateService allocateService) {
        this.allocateService = allocateService;
    }

    @GetMapping("/run-fragmentation")
    public void runFragment() {
        allocateService.allocateBigObject();
    }

    @GetMapping("/run-throughput")
    public void runThroughput(@RequestParam(defaultValue = "10") int size) {
        allocateService.createBatchObjects(size);
    }
}
