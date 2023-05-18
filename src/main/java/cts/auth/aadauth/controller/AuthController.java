package cts.auth.aadauth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    @GetMapping("public")
    public String publicEndpoint() {
        return "This is a public endpoint";
    }

    @PreAuthorize("hasRole('ROLE_Developer')")
    @GetMapping("dev")
    public String devEndpoint(){
        return "Only Developer Group users can access this endpoint";
    }
}
