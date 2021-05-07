package com.schneider;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.vavr.control.Option;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @RestController
    @RequestMapping("/api/afd/client")
    class AfdClientController {

        private final AfdService afdService;

        public AfdClientController(AfdService afdService) {
            this.afdService = afdService;
        }

        @GetMapping("/user")
        @PreAuthorize("hasRole('ROLE_carrierGuest')")
        @CircuitBreaker(name = "main_service", fallbackMethod = "fallback")
        public ResponseEntity<List<SniUser>> getUser(@RequestParam(value = "role", required = false) String role,
                                                     @RequestParam(value = "carrier", required = false) String carrierId,
                                                     @RequestParam(value = "phone", required = false) String phone) {

            Option<List<SniUser>> members = this.afdService.getMembers(role, carrierId, phone);
            return members
                    .map(ResponseEntity::ok)
                    .getOrElse(() -> ResponseEntity.notFound().build());
        }

        // circuit breaker
        public ResponseEntity<String> fallback(String role,
                                               String carrier,
                                               String phone,
                                               Exception exception) {
            return new ResponseEntity<String>("AFD Service is having issues in responding", HttpStatus.OK);
        }
    }
}