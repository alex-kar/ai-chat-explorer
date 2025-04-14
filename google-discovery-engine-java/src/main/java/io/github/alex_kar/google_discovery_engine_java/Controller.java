package io.github.alex_kar.google_discovery_engine_java;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@Slf4j
public class Controller {

    @PostMapping("/session/{sessionId}/question")
    public Answer question(@PathVariable String sessionId, @RequestBody Question question) {
        return Answer.builder()
                .text("Answer")
                .build();
    }

}
