package cts.auth.aadauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @GetMapping("/endpoint1")
    public String endpoint1(){
        return "from /api/public/endpoint1";
    }
    @GetMapping("/token")
    public void token(@RequestParam String token) {
        System.out.println(token);
    }
}
