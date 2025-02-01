package gymnote.gymnoteapi.controller;

import gymnote.gymnoteapi.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloContoller {

    @GetMapping("/")
    public String greeting() {
        return "Hello";
    }

    @GetMapping("/private")
    public String securedEndpoint(@AuthenticationPrincipal UserPrincipal principal) {
        return "this is secured route for user: " + principal.getEmail() + " ID: " + principal.getUserId();
    }
}
