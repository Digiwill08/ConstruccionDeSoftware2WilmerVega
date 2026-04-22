package gestiondeunbanco.wilmervega.application.adapters.api;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController()
public class Ping {

    @GetMapping("/ping")
    public String ping() {
        return "Pong";
    }

}
