package cts.auth.aadauth.controller;



import cts.auth.aadauth.dto.LoginDTO;
import cts.auth.aadauth.exceptions.ApiException;
import cts.auth.aadauth.services.AadResourceOwnerPasswordCredentials;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/")
@CrossOrigin
public class AuthController {
    private final AadResourceOwnerPasswordCredentials loginService;

    public AuthController(AadResourceOwnerPasswordCredentials loginService) {
        this.loginService = loginService;
    }

    /**
     * Profile: test
     * This is a public endpoint for testing
     * When the profile is set 'test' this endpoint can be accessed to
     * check if unsecured calls work
     *
     * @return
     */
    @GetMapping("public")
    public String publicEndpoint() {
        return "This is a public endpoint";
    }
    /**
     * Profile: test
     * This is a secured endpoint for testing
     * When the profile is set 'test' this endpoint can be accessed to
     * check if a JWT has the Developer Role in its claims calls work
     *
     * @return
     * Returns a string
     */
    //Secured Endpoint
    @PreAuthorize("hasRole('ROLE_Developer')")
    @GetMapping("dev")
    public String devEndpoint() {
        return "Only Developer Group users can access this endpoint";
    }

    //Endpoint for obtaining a jwt token from Azure Active Directory
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody @Valid LoginDTO loginDTO) throws IOException, ApiException, MethodArgumentNotValidException, NoSuchMethodException {
        HashMap<String, String> map = new HashMap<>();
        map.put("token", loginService.getToken(loginDTO));
        return map;
    }
}
