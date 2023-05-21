package cts.auth.aadauth.controller;



import cts.auth.aadauth.dto.LoginDTO;
import cts.auth.aadauth.exceptions.ApiException;
import cts.auth.aadauth.services.LoginService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth/")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    private final LoginService loginService;

    public AuthController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("public")
    public String publicEndpoint() {
        return "This is a public endpoint";
    }

    @PreAuthorize("hasRole('ROLE_Developer')")
    @GetMapping("dev")
    public String devEndpoint() {
        return "Only Developer Group users can access this endpoint";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO loginDTO) throws IOException, ApiException {
        return loginService.getToken(loginDTO);

    }
}
