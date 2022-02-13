package leesh.devcom.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import leesh.devcom.backend.security.CustomUserDetailsService;
import leesh.devcom.backend.security.JwtFilter;
import leesh.devcom.backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // enable h2-console
                .headers()
                    .frameOptions()
                    .sameOrigin()
                    .and()
                .cors()
                    .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .csrf()
                    .disable()
                .formLogin()
                    .disable()
                .httpBasic()
                    .disable()
                .authorizeRequests()
                    .antMatchers(
                        "/", "/error", "/favicon.ico",
                        "/**/*.png", "/**/*.gif", "/**/*.svg",
                        "/**/*.jpg", "/**/*.html", "/**/*.css", "/**/*.js", "/h2-console/**")
                        .permitAll()
                    .antMatchers("/api/auth/login", "/api/auth/register")
                        .permitAll()
                .anyRequest()
                    .authenticated();

        httpSecurity.addFilterBefore(new JwtFilter(objectMapper, jwtUtil), UsernamePasswordAuthenticationFilter.class);
    }

}
