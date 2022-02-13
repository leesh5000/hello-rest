package leesh.devcom.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import leesh.devcom.backend.common.ServerProperty;
import leesh.devcom.backend.domain.Member;
import leesh.devcom.backend.dto.LoginRequest;
import leesh.devcom.backend.dto.RegisterRequest;
import leesh.devcom.backend.exception.CustomException;
import leesh.devcom.backend.exception.ErrorCode;
import leesh.devcom.backend.security.CustomUserDetailsService;
import leesh.devcom.backend.security.JwtUtil;
import leesh.devcom.backend.service.MemberService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebAppConfiguration
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AuthController authController;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    ServerProperty serverProperty;

    @Autowired
    MemberService memberService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp(WebApplicationContext wac, RestDocumentationContextProvider restDocument) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocument)
                        .uris()
                            .withScheme(serverProperty.getScheme())
                            .withHost(serverProperty.getAddress())
                            .withPort(serverProperty.getPort())
                        .and()
                        .operationPreprocessors()
                            .withRequestDefaults(prettyPrint())
                            .withResponseDefaults(prettyPrint()))
                .build();
    }

    @AfterEach
    void tearDown() {

    }

    @DisplayName("login unit test")
    @Test
    void login_unit_test() {

        // given
        LoginRequest requestDto = LoginRequest.builder()
                .email("test1@gmail.com")
                .password("1111")
                .build();

        HttpServletResponse mock = mock(HttpServletResponse.class);

        // when
        ResponseEntity<?> login = authController.login(requestDto, mock);
    }

    @DisplayName("[login integration test] Success")
    @Test
    public void login_integration_test_ok() throws Exception {

        // given
        LoginRequest data = LoginRequest.builder()
                .email("test1@gmail.com")
                .password("1111")
                .build();

        // when
        ResultActions result = mockMvc. perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(data)));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("x-auth=")))
                .andExpect(jsonPath("access_token").exists())
                .andExpect(jsonPath("expiry_sec").exists())
                .andExpect(jsonPath("_links.self.href").exists())
                .andExpect(jsonPath("_links.profile.href").exists())
                .andExpect(jsonPath("_links.logout.href").exists())
                .andDo(print());

        // create rest docs snippets
        result
                .andDo(document("login",
//                        getDocumentRequest(),
//                        getDocumentResponse(),
                        links(
                                halLinks(),
                                linkWithRel("self").description("link to self"),
                                linkWithRel("profile").description("link to profile"),
                                linkWithRel("logout").description("link to logout")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("user email"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("user password")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type"),
                                headerWithName(HttpHeaders.SET_COOKIE).description("refresh token")
                        ),
                        responseFields(
                                fieldWithPath("access_token").type(JsonFieldType.STRING).description("access token"),
                                fieldWithPath("expiry_sec").type(JsonFieldType.NUMBER).description("Expiration time of access token"),
                                fieldWithPath("_links.self.href").type(JsonFieldType.STRING).description("link to self"),
                                fieldWithPath("_links.profile.href").type(JsonFieldType.STRING).description("link to profile"),
                                fieldWithPath("_links.logout.href").type(JsonFieldType.STRING).description("link to logout")
                        )
                ));
    }

    @DisplayName("login test failed - invalid input value")
    @MethodSource(value = "params_loginTest_failed_01")
    @ParameterizedTest
    public void loginTest_failed_01(String email, String password) throws Exception {
        // given
        LoginRequest data = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();

        // when
        ResultActions result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(data)));

        // then
        result
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(res -> assertThat(Objects.requireNonNull(res.getResolvedException()).getClass()).isEqualTo(MethodArgumentNotValidException.class))
                .andExpect(jsonPath("status").exists())
                .andExpect(jsonPath("code").exists())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("errors").exists())
                .andExpect(jsonPath("errors[0].field").exists())
                .andExpect(jsonPath("errors[0].value").exists())
                .andExpect(jsonPath("errors[0].reason").exists())
                .andExpect(jsonPath("_links.index.href").exists())
                .andExpect(jsonPath("_links.profile.href").exists())
                .andDo(print());

        // create rest docs snippets
        result
                .andDo(document("4000001",
//                        getDocumentRequest(),
//                        getDocumentResponse(),
                        links(
                                halLinks(),
                                linkWithRel("index").description("link to index"),
                                linkWithRel("profile").description("link to error document")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("status").optional().type(JsonFieldType.STRING).description("http status"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("error code"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("error message"),
                                fieldWithPath("errors[]").type(JsonFieldType.ARRAY).description("errors"),
                                fieldWithPath("errors[].field").type(JsonFieldType.STRING).description("error field"),
                                fieldWithPath("errors[].value").type(JsonFieldType.STRING).description("error input"),
                                fieldWithPath("errors[].reason").type(JsonFieldType.STRING).description("error reason"),
                                fieldWithPath("_links.index.href").type(JsonFieldType.STRING).description("index url"),
                                fieldWithPath("_links.profile.href").type(JsonFieldType.STRING).description("document url")
                        )
                ));
    }

    static Stream<Arguments> params_loginTest_failed_01() {
        return Stream.of(
                Arguments.arguments("", ""),
                Arguments.arguments("test1@gmail.com", ""),
                Arguments.arguments(" ", " "),
                Arguments.arguments("aa", "1111")
        );
    }

    @DisplayName("logout unit test")
    @Test
    void logout_unit_test() {
        ResponseEntity<?> logout = authController.logout("refresh_token");
    }

    @DisplayName("logout integration test - 200 ok")
    @Test
    void logout_integration_test_200_ok() throws Exception {

        // given
        String email = "test1@gmail.com";
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        String accessToken = jwtUtil.createAccessToken(userDetails);
        String refreshToken = jwtUtil.createRefreshToken(accessToken);
        Cookie cookie = new Cookie("x-auth", refreshToken);

        // when
        ResultActions result = mockMvc.perform(get("/api/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .cookie(cookie));

        // then
        result
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.self.href").exists())
                .andExpect(jsonPath("_links.login.href").exists())
                .andExpect(jsonPath("_links.index.href").exists())
                .andExpect(jsonPath("_links.profile.href").exists())
                .andDo(print());

        // create rest docs snippets
        result
                .andDo(document("logout",
                        links(
                                halLinks(),
                                linkWithRel("self").description("link to self"),
                                linkWithRel("login").description("link to login"),
                                linkWithRel("index").description("link to index"),
                                linkWithRel("profile").description("link to document")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("access token")
//                                headerWithName(HttpHeaders.COOKIE).description("cookie")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("_links.self.href").type(JsonFieldType.STRING).description("self url"),
                                fieldWithPath("_links.index.href").type(JsonFieldType.STRING).description("index url"),
                                fieldWithPath("_links.login.href").type(JsonFieldType.STRING).description("login url"),
                                fieldWithPath("_links.profile.href").type(JsonFieldType.STRING).description("document url")
                        )
                ));
    }

    @DisplayName("register integration test - 201 OK")
    @Test
    void register_integration_test() throws Exception {

        // given
        RegisterRequest requestDto = RegisterRequest.builder()
                .email("leesh@gmail.com")
                .username("leesh")
                .password("1111")
                .build();

        // when
        ResultActions result = mockMvc. perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(requestDto)));

        // then
        result
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").isNumber())
                .andExpect(jsonPath("_links.self.href").exists())
                .andExpect(jsonPath("_links.profile.href").exists())
                .andExpect(jsonPath("_links.login.href").exists())
                .andDo(print());

        // create rest docs snippets
        result
                .andDo(document("register",
                        links(
                                halLinks(),
                                linkWithRel("self").description("link to self"),
                                linkWithRel("login").description("link to logout"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("user email"),
                                fieldWithPath("username").type(JsonFieldType.STRING).description("user name"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("user password")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("user key id"),
                                fieldWithPath("_links.self.href").type(JsonFieldType.STRING).description("link to self"),
                                fieldWithPath("_links.profile.href").type(JsonFieldType.STRING).description("link to profile"),
                                fieldWithPath("_links.login.href").type(JsonFieldType.STRING).description("link to login")
                        )
                ));
    }

    @DisplayName("register integration test - 409 conflict")
    @Test
    void register_integration_test_409() throws Exception {
        // given
        String email = "leesh@gmail.com";
        String username = "leesh";
        String password = "1111";

        Member member = Member.createMember(email, username, password);
        memberService.save(member);

        RegisterRequest requestDto = RegisterRequest.builder()
                .email(email)
                .username(username)
                .password(password)
                .build();

        // when
        ResultActions perform = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(requestDto)));

        // then
        ResultActions actions = perform
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isConflict())
                .andExpect(res -> assertThat(Objects.requireNonNull(res.getResolvedException()).getClass()).isEqualTo(CustomException.class))
                .andExpect(jsonPath("status").exists())
                .andExpect(jsonPath("code").exists())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("errors").isEmpty())
                .andDo(print());

        // create docs
        actions
                .andDo(document(String.valueOf(ErrorCode.ALREADY_EXIST_MEMBER.getCode()),
                        links(
                                halLinks(),
                                linkWithRel("index").description("link to index"),
                                linkWithRel("profile").description("link to document")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("user email"),
                                fieldWithPath("username").type(JsonFieldType.STRING).description("user name"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("user password")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("status").optional().type(JsonFieldType.STRING).description("http status"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("error code"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("error message"),
                                fieldWithPath("errors[]").type(JsonFieldType.ARRAY).description("errors"),
                                fieldWithPath("_links.index.href").type(JsonFieldType.STRING).description("link to index"),
                                fieldWithPath("_links.profile.href").type(JsonFieldType.STRING).description("link to profile")
                        )
                ));
    }
}