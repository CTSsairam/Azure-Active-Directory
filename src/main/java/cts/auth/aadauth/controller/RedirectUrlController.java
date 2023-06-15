package cts.auth.aadauth.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class RedirectUrlController {
    //redirect url
    @GetMapping("/token")
    public void token(@RequestParam String token) {
        System.out.println(token);
    }
}
