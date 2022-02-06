package leesh.devcom.backend.controller;

import leesh.devcom.backend.common.GlobalProperties;
import leesh.devcom.backend.dto.LoginRequest;
import leesh.devcom.backend.dto.LoginResponse;
import leesh.devcom.backend.security.CustomUserDetailsService;
import leesh.devcom.backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static leesh.devcom.backend.security.JwtUtil.ACCESS_TOKEN_EXPIRED_SEC;
import static leesh.devcom.backend.security.JwtUtil.REFRESH_TOKEN_EXPIRED_SEC;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;
    private final GlobalProperties globalProperties;

    private static final String X_AUTH = "x-auth";

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody @Validated final LoginRequest requestDto, HttpServletResponse response) {

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword()));

        String accessToken = jwtUtil.createAccessToken(authenticate);
        String refreshToken = jwtUtil.createRefreshToken(authenticate);

        // set cookie
        Cookie cookie = new Cookie(X_AUTH, refreshToken);
        cookie.setMaxAge(Math.toIntExact(REFRESH_TOKEN_EXPIRED_SEC));
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        // send response
        LoginResponse build = LoginResponse.builder()
                .accessToken(accessToken)
                .expirySec(ACCESS_TOKEN_EXPIRED_SEC)
                .build();

        EntityModel<LoginResponse> payload = EntityModel.of(build,
                linkTo(methodOn(AuthController.class).login(requestDto, response)).withSelfRel(),
                Link.of("index.html#_login_response").withRel("profile")
                );

        return ResponseEntity.ok(payload);
    }
}
