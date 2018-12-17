package io.fonimus.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @GetMapping("/api/status")
    public ResponseEntity<Void> status(@RequestParam(name = "status", required = false) Integer status) {
        if (status != null) {
            return ResponseEntity.status(status).build();
        }
        return ResponseEntity.ok().build();
    }
}
