package leesh.devcom.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import leesh.devcom.backend.controller.IndexController;
import leesh.devcom.backend.exception.CustomException;
import leesh.devcom.backend.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Filter;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@Filter(name = "JwtFilter")
public class JwtFilter extends OncePerRequestFilter {

    public static final String BEARER = "Bearer ";
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    public JwtFilter(ObjectMapper objectMapper, JwtUtil jwtUtil) {
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {

        log.info("jwt filter start");
        Optional<String> optional = getAccessToken(request);
        if (optional.isPresent()) {
            String accessToken = optional.get();
            try {
                jwtUtil.validate(accessToken);
                Authentication authentication = jwtUtil.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("SecurityContext saved '{}' uri: {}", authentication.getName(), request.getRequestURI());
            } catch (CustomException e) {
                log.error("Jwt validate failed", e);
                EntityModel<ErrorResponse> body = this.halProcess(ErrorResponse.of(e.getErrorCode()));
                response.setStatus(e.getErrorCode().getStatus().value());
                response.getWriter().write(objectMapper.writeValueAsString(body));
                return;
            }
        } else {
            log.info("not exist access token");
        }
        filterChain.doFilter(request, response);
    }

    private Optional<String> getAccessToken(@NotNull HttpServletRequest request) {

        return Optional.ofNullable(request.getHeader("Authorization"))
                .filter(token -> StringUtils.hasText(token) && token.startsWith(BEARER))
                .map(token -> token.substring(BEARER.length()));
    }

    private EntityModel<ErrorResponse> halProcess(@NotNull ErrorResponse payload) {
        return EntityModel.of(payload,
                linkTo(IndexController.class).withRel("index"),
               Link.of("http://localhost:18080/docs/index.html#" + payload.getCode()).withRel("profile"));
    }
}

