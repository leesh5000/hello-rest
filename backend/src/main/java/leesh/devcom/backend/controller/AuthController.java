package leesh.devcom.backend.controller;

import leesh.devcom.backend.domain.Member;
import leesh.devcom.backend.dto.LoginRequest;
import leesh.devcom.backend.dto.LoginResponse;
import leesh.devcom.backend.dto.RegisterRequest;
import leesh.devcom.backend.dto.RegisterResponse;
import leesh.devcom.backend.dto.assembler.LoginResponseAssembler;
import leesh.devcom.backend.security.CustomUserDetailsService;
import leesh.devcom.backend.security.JwtUtil;
import leesh.devcom.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static leesh.devcom.backend.security.JwtUtil.ACCESS_TOKEN_EXPIRED_SEC;
import static leesh.devcom.backend.security.JwtUtil.REFRESH_TOKEN_EXPIRED_SEC;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(produces = MediaTypes.HAL_JSON_VALUE)
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;
    private final LoginResponseAssembler assembler;
    private static final String X_AUTH = "x-auth";

    @PostMapping("/api/auth/register")
    public ResponseEntity<?> register(@RequestBody @Validated final RegisterRequest requestDto) {

        Member member = Member.createMember(requestDto.getEmail(), requestDto.getUsername(), passwordEncoder.encode(requestDto.getPassword()));
        Long id = memberService.save(member);

        RegisterResponse data = RegisterResponse.builder()
                .id(id)
                .build();

        EntityModel<RegisterResponse> body = EntityModel.of(data,
                linkTo(methodOn(AuthController.class).register(requestDto)).withSelfRel(),
                linkTo(methodOn(AuthController.class).login(null, null)).withRel("login"),
                Link.of("http://localhost:18080/docs/index.html#register").withRel("profile"));

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping("/api/auth/login")
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
        LoginResponse payload = LoginResponse.builder()
                .accessToken(accessToken)
                .expirySec(ACCESS_TOKEN_EXPIRED_SEC)
                .build();

        return ResponseEntity.ok(assembler.toModel(payload));
    }

    @GetMapping("/api/auth/logout")
    public ResponseEntity<?> logout(@NotNull @CookieValue(name = X_AUTH) String refreshToken) {

        redisTemplate.opsForValue().getAndDelete(refreshToken);
        RepresentationModel<?> body = new RepresentationModel<>();
        body.add(
                linkTo(methodOn(AuthController.class).logout(refreshToken)).withSelfRel(),
                linkTo(methodOn(AuthController.class).login(null, null)).withRel("login"),
                linkTo(methodOn(IndexController.class).index()).withRel("index"),
                Link.of("http://localhost:18080/docs/index.html#logout").withProfile("profile")
        );
        return ResponseEntity.ok(body);
    }
}
