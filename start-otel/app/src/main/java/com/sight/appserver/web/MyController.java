package com.sight.appserver.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MyController {

    private static final Logger logger = LoggerFactory.getLogger(MyController.class);

    @Value("${TARGET_SERVER_1_HOST:http://localhost:8080}")
    private String TARGET_SERVER_1_HOST;

    @Value("${TARGET_SERVER_2_HOST:http://localhost:8080}")
    private String TARGET_SERVER_2_HOST;

    @GetMapping("/")
    public String root(@RequestParam(defaultValue = "Sight") String name, HttpRequest request) {
        logger.info("root");
        logger.warn("root called");
        logger.error("root called has error {}", request.toString());
        logger.info("Hello {}", name);

        return "Hello %S".formatted(name);
    }

    @GetMapping("/io_task")
    public String io_task(@RequestParam(defaultValue = "Sight") String name) throws InterruptedException {
        Thread.sleep(1500);
        logger.info("io_task by {}", name);
        return "io_task";
    }

    @GetMapping("/cpu_task")
    public String cpu_task(@RequestParam(defaultValue = "Sight") String name) {
        for (int i = 0; i < 100; i++) {
            int tmp = i * i * i;
        }

        logger.info("cpu_task by {}", name);
        return "cpu_task";
    }

    @GetMapping("/chain_link")
    public String chain_link(@RequestParam(defaultValue = "Sight") String name) {
        logger.info("chain_link by {}", name);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForEntity("http://localhost:8080/", String.class);
        restTemplate.getForEntity("%s/io_task".formatted(TARGET_SERVER_1_HOST), String.class);
        restTemplate.getForEntity("%s/cpu_task".formatted(TARGET_SERVER_2_HOST), String.class);
        return "chain_link";
    }
}
