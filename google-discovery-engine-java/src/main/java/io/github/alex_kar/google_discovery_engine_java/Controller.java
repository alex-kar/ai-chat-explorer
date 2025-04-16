package io.github.alex_kar.google_discovery_engine_java;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@CrossOrigin(origins = { "http://localhost:8080", "http://localost:8081" })
@RestController
@Slf4j
public class Controller {

    @PostMapping("/session/{sessionId}/question")
    public Answer question(@PathVariable String sessionId, @RequestBody Question question) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Answer.builder()
                .text("Answer")
                .build();
    }

}
